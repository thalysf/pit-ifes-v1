import {Component, EventEmitter, Input, NgModule, OnChanges, Output, SimpleChanges} from "@angular/core";
import {DxTextBoxModule} from "devextreme-angular";

@Component({
    selector: 'seletor-unico',
    templateUrl: 'seletor-unico.html',
    styleUrls: ['seletor-unico.css']
})
export class SeletorUnico {

    @Input() textoInput: string = '';
    @Input() disabled = false;
    @Input() placeholder = 'Clique para buscar';
    @Input() readOnly = false;

    @Output() onClick = new EventEmitter<any>();
    @Output() textoInputChange = new EventEmitter<any>();

    searchButton;

    constructor() {
        this.searchButton = {
            icon: 'search',
            type: 'default',
            onClick: this.clickEvent
        }
    }

    clickEvent = () => {
        if(!this.disabled) this.onClick.emit();
    }
}

@NgModule({
    imports: [
        DxTextBoxModule
    ],
    declarations: [SeletorUnico],
    exports: [SeletorUnico]
})
export class SeletorUnicoModule {
}
