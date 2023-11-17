import {Component, EventEmitter, Input, NgModule, OnChanges, Output, SimpleChanges} from '@angular/core';
import {CommonModule} from "@angular/common";
import {DxMenuModule, DxSelectBoxModule, DxTextBoxModule} from "devextreme-angular";
import {FormsModule} from "@angular/forms";
import {DxiMenuItemModule} from "devextreme-angular/ui/nested";
import notify from "devextreme/ui/notify";
import DataSource from "devextreme/data/data_source";
import {
    doubleToCargaHorariaComponent,
    doubleToDateStringFormat, incrementHora,
    stringHourFormatToDouble
} from "../../comum/cargaHoraria.resource";

export interface HoraConfiguration {
    id: string,
    nome: string
}

@Component({
    selector: 'app-carga-horaria-editor',
    templateUrl: './carga-horaria-editor.component.html',
})
export class CargaHorariaEditorComponent implements OnChanges{
    @Input() width: number | undefined;
    @Input() showClearButton = true;
    @Input() value: HoraConfiguration = {id: '00:00', nome: '00:00'};
    @Input() min: number | undefined;
    @Input() max: number | undefined;
    @Input() step: number = 0.3;
    @Input() readOnly = false;

    @Output() valueChange = new EventEmitter<any>();
    @Output() onValueChanged = new EventEmitter<any>();

    horasDisponiveisSelect!: DataSource;

    constructor() {
        this.populateHorasDisponiveisSelect();
    }

    ngOnChanges(changes: SimpleChanges): void {
        if(changes['min'] || changes['max']){
            this.populateHorasDisponiveisSelect();
        }
    }

    populateHorasDisponiveisSelect() {
        this.horasDisponiveisSelect = new DataSource({
            store: <HoraConfiguration[]>[],
            sort: [
                {selector: 'id', desc: false}
            ]
        });

        let i = 0;
        while (i <= 40) {
            if (this.validateMinAndMax(i)) {
                const horaConfig = doubleToCargaHorariaComponent(i);
                this.criarNovaHoraArray(horaConfig);
            } else if (this.max && i > this.max) {
                const horaConfig = doubleToCargaHorariaComponent(this.max);
                this.criarNovaHoraArray(horaConfig);
            }
            i = incrementHora(i, this.step);
        }
    }

    createNewHora = ($event: any) => {
        const created: string = $event.text;
        if (created == '') return;

        let hora: HoraConfiguration = {id: created, nome: created};

        if (!this.isValidHourFormat(created)) {
            console.log('zerando hora');
            hora = {id: '00:00', nome: '00:00'};
            notify('Hora inválida', 'error', 2000);
        }

        hora = this.formatarHora(hora.id); // se 03:68 => 03:59
        if(!this.validateMin(stringHourFormatToDouble(hora.id))){ // se menor que o minimo, arredondar para minimo
            hora = this.formatarHora(doubleToDateStringFormat(this.min!));
        }
        if (!this.validateMax(stringHourFormatToDouble(hora.id))) { // se maior que maximo, arredondar para maximo
            hora = this.formatarHora(doubleToDateStringFormat(this.max!));
        }

        this.criarNovaHoraArray(hora);
        $event.customItem = hora;
    }

    /**
     * Caso o minuto seja maior que 59 ele volta para 59
     * @param text
     */
    formatarHora(text: string): HoraConfiguration {
        let [hora, minuto] = text.split(':');
        if (Number(minuto) > 59) {
            minuto = '59';
        }
        const horaFormatted = `${hora}:${minuto}`;
        return {id: horaFormatted, nome: horaFormatted};
    }

    validateMin(double: number){
        if (this.min != undefined && double < this.min) return false;
        return true;
    }

    validateMax(double: number) {
        if (this.max != undefined && double > this.max) return false;
        return true;
    }

    validateMinAndMax(double: number): boolean {
        return this.validateMin(double) && this.validateMax(double);
    }

    isValidHourFormat(str: string) {
        // Using a regular expression to match the "##:##" format
        const regex = /^\d{2}:\d{2}$/;
        return regex.test(str);
    }

    private criarNovaHoraArray(hora: HoraConfiguration) {
        if (!this.horasDisponiveisSelect.items().find(h => h.id == hora.id)) {
            this.horasDisponiveisSelect.store().insert(hora);
        }
        this.horasDisponiveisSelect.reload();
    }

    private isHoraValidaChanged(hora: string): boolean{
        return this.isValidHourFormat(hora) && this.validateMinAndMax(stringHourFormatToDouble(hora));
    }

    changeValue = ($event: any) => {
        if ($event.selectedItem) {
            const value: HoraConfiguration = this.horasDisponiveisSelect.items().find(h => h.id == $event.selectedItem.id);

            if(this.isHoraValidaChanged(value.id)){
                this.valueChange.emit(value);
                this.onValueChanged.emit(value);
            }
        }
    }
}

@NgModule({
    imports: [
        CommonModule,
        DxTextBoxModule,
        FormsModule,
        DxiMenuItemModule,
        DxSelectBoxModule,
        DxMenuModule,
    ],
    declarations: [CargaHorariaEditorComponent],
    exports: [CargaHorariaEditorComponent]
})
export class CargaHorariaEditorModule {
}
