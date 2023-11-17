import {Component, Input, Output} from '@angular/core';
import {Observable} from "rxjs";
import {tap} from "rxjs/operators";
import {EventEmitter} from "@angular/core";


@Component({
    selector: 'modal-padrao',
    templateUrl: './modal-padrao.component.html',
    styleUrls: ['./modal-padrao.component.scss']
})
export class ModalPadraoComponent {

    @Input() isVisible = false;
    @Input() titulo!: string;
    @Input() salvar!: Observable<any>;
    @Input() textBtnSalvar: string = "Salvar";
    @Input() width = 1000;

    @Output() isVisibleChange: EventEmitter<boolean> = new EventEmitter<boolean>();

    iconBtnSave = 'save';
    btnSaveDisabled = false;


    fechar() {
        this.isVisible = false;
        this.isVisibleChange.emit(this.isVisible);
    }

    abrir() {
        this.isVisible = true;
        this.isVisibleChange.emit(this.isVisible);
    }

    onSalvar() {
        this.iconBtnSave = 'zmdi zmdi-spinner zmdi-hc-spin';
        this.btnSaveDisabled = true;

        this.salvar.pipe(tap(
            response => {
                this.iconBtnSave = 'save';
                this.btnSaveDisabled = false;
            }, error => {
                this.iconBtnSave = 'save';
                this.btnSaveDisabled = false;
            }
        )).subscribe()
    }
}
