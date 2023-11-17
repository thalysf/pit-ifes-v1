import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ModalPadraoComponent} from './modal-padrao.component';
import {DxButtonModule, DxPopupModule, DxScrollViewModule} from "devextreme-angular";


@NgModule({
    declarations: [
        ModalPadraoComponent
    ],
    exports: [
        ModalPadraoComponent
    ],
    imports: [
        CommonModule,
        DxPopupModule,
        DxScrollViewModule,
        DxButtonModule
    ]
})
export class ModalPadraoModule {
}
