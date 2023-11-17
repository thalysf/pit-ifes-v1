import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {UploadButtonComponent} from "./upload-button.component";
import {DxFileUploaderModule, DxLoadIndicatorModule} from 'devextreme-angular';

@NgModule({
    declarations: [UploadButtonComponent],
    imports: [
        CommonModule,
        DxFileUploaderModule,
        DxLoadIndicatorModule
    ],
    exports: [UploadButtonComponent]
})
export class UploadButtonModule {
}
