import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {PortariaRoutingModule} from './portaria-routing.module';
import {PortariaComponent} from "./portaria.component";
import {EditComponentModule} from "../../shared/components/edit-component/edit-component.component";
import {TableAssociacaoModule} from "../../shared/components/table-list/table-associacao/table-associacao.component";
import {DxDataGridModule, DxTextBoxModule} from "devextreme-angular";
import {AtividadeAssociadaModule} from "./atividade-associada/atividade-associada.component";
import {ToolbarModule} from "../../shared/components/toolbar/toolbar.component";
import {UploadButtonModule} from "../../shared/components/upload-button/upload-button.module";
import { ImportarPortariaComponent } from './importar-portaria/importar-portaria.component';
import {ImportarModule} from "../../shared/components/importar/importar.component";
import {ProfessoresPortariaModule} from "./professores-portaria/professores-portaria.module";


@NgModule({
    declarations: [
        PortariaComponent,
        ImportarPortariaComponent
    ],
    exports: [],
    imports: [
        CommonModule,
        PortariaRoutingModule,
        EditComponentModule,
        TableAssociacaoModule,
        DxTextBoxModule,
        DxDataGridModule,
        AtividadeAssociadaModule,
        ToolbarModule,
        UploadButtonModule,
        ImportarModule,
        ProfessoresPortariaModule,
    ]
})
export class PortariaModule {
}
