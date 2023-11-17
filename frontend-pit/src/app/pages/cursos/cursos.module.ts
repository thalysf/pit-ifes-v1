import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {CursosRoutingModule} from "./cursos-routing.module";
import {ToolbarModule} from "../../shared/components/toolbar/toolbar.component";
import {UploadButtonModule} from "../../shared/components/upload-button/upload-button.module";
import { ImportarCursosComponent } from './importar-cursos/importar-cursos.component';
import {ImportarModule} from "../../shared/components/importar/importar.component";

@NgModule({
    declarations: [
        ImportarCursosComponent
    ],
    imports: [
        CommonModule,
        CursosRoutingModule,
        ToolbarModule,
        UploadButtonModule,
        ImportarModule,
    ]
})
export class CursosModule {
}
