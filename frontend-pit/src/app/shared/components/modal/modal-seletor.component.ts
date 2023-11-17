import {Component, EventEmitter, Input, NgModule, OnChanges, OnDestroy, Output, SimpleChanges} from "@angular/core";
import {DxButtonModule, DxDataGridModule, DxPopupModule, DxSelectBoxModule, DxTextBoxModule} from "devextreme-angular";
import {ConfigurationColumn} from "../../model/resource";
import {DxoSelectionModule} from "devextreme-angular/ui/nested";
import {HttpClientModule} from "@angular/common/http";
import {ApiService} from "../../services/api.service";
import {CommonModule} from "@angular/common";
import notify from "devextreme/ui/notify";
import {SeletorUnicoModule} from "./seletor-unico/seletor-unico";
import {ModalPadraoModule} from "../modal-padrao/modal-padrao.module";
import {Observable} from "rxjs";


@Component({
    selector: 'modal-seletor',
    templateUrl: 'modal-seletor.component.html',
    styleUrls: [
        'modal-seletor.component.css',
        'tabela-seletor.component.css',
        '../formulario-dinamico/formulario-dinamico.component.css',
        '../table-list/tabela-listagem/tabela-listagem.component.scss'
    ]
})
export class ModalSeletorComponent implements OnDestroy, OnChanges {

    @Input() colunas!: ConfigurationColumn[];
    @Input() route!: string;
    @Input() valueExpr!: string;
    @Input() displayExpr!: string;
    @Input() selectedItem!: any;
    @Input() titulo = '';
    @Input() width = 1000;
    @Input() displayFunction?: Function | null = null;
    @Input() disabled = false;
    @Input() readOnly = false;
    @Input() placeholder = 'Clique para buscar';

    @Input() columnsFilter: string[] = [];
    @Output() selectedItemChange: EventEmitter<any> = new EventEmitter<any>();

    isPopupVisible: boolean;
    entidades: any[] = [];
    selectedKeys: any = {};
    apiSubscription: any;
    textoInput: string = '';
    itemsSelectedObject: any = null;
    filtro = '';

    constructor(
        private apiService: ApiService
    ) {
        this.isPopupVisible = false;
    }

    public openModal() {
        this.isPopupVisible = true;
        this.preSelecionar();
        this.listarEntidades();
    }

    private preSelecionar(){
        if(this.selectedItem){
            this.selectedKeys = this.selectedItem;
        } else {
            this.selectedKeys = {};
        }
    }

    public listarEntidades() {
        const params: any = {};
        this.columnsFilter.forEach((coluna: string) => {
            params[coluna] = this.filtro;
        })

        this.apiSubscription = this.apiService.get(this.route, params).subscribe(
            response => {
                this.entidades = response as any[];
            }
        );
    }

    ngOnDestroy(): void {
        this.entidades = [];
        if (this.apiSubscription) this.apiSubscription.unsubscribe();
    }

    ngOnChanges(changes: SimpleChanges): void {
        if(changes['selectedItem']){
            this.setTextoTextarea();
        }
    }

    private setTextoTextarea(){
        if(this.selectedItem) {
            if(!this.displayFunction) this.textoInput = this.getValueFromNestedProperty(this.selectedItem, this.displayExpr);
            else this.textoInput = this.displayFunction(this.selectedItem);
        }
        else this.textoInput = '';
    }

    private getValueFromNestedProperty(obj: any, path: string) {
        const properties = path.split('.');
        let value = obj;

        for (const prop of properties) {
            if (value.hasOwnProperty(prop)) {
                value = value[prop];
            } else {
                return undefined; // Propriedade aninhada não encontrada
            }
        }

        return value;
    }

    private closeModal() {
        this.isPopupVisible = false;
    }

    public cancelar() {
        this.closeModal();
    }

    public salvar = () => {
        return new Observable(observer => {
            if (this.selectedKeys.length == 0) {
                notify(
                    {
                        message: "Selecione um item da tabela!",
                        width: 230,
                        position: {
                            at: "bottom",
                            my: "bottom",
                            of: "#container",
                        }
                    },
                    'error',
                    1000
                );
                observer.error();
                return;
            }
            this.itemsSelectedObject = this.selectedKeys; // objeto com texto e valor selecionado
            // @ts-ignore
            // this.textoInput = this.selectedKeys[this.displayExpr]; // texto selecionado
            // @ts-ignore
            this.selectedItem = this.selectedKeys;

            this.selectedItemChange.emit(this.selectedItem);
            this.closeModal();
            observer.next();
        })
    }

    public onSelectionChanged(selectedItems: any) {
        this.selectedKeys = selectedItems.currentSelectedRowKeys[0];
    }
}

@NgModule({
    imports: [
        DxPopupModule,
        DxDataGridModule,
        DxoSelectionModule,
        HttpClientModule,
        CommonModule,
        DxButtonModule,
        DxSelectBoxModule,
        SeletorUnicoModule,
        ModalPadraoModule,
        DxTextBoxModule
    ],
    declarations: [ModalSeletorComponent],
    exports: [ModalSeletorComponent]
})
export class ModalSeletorModule {
}
