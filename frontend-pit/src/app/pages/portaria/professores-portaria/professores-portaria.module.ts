import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProfessoresPortariaComponent } from './professores-portaria.component';
import {TabelaListagemModule} from "../../../shared/components/table-list/tabela-listagem/tabela-listagem.component";
import {ModalPadraoModule} from "../../../shared/components/modal-padrao/modal-padrao.module";
import {ModalSeletorModule} from "../../../shared/components/modal/modal-seletor.component";
import {SelectModule} from "../../../shared/components/select/select.component";



@NgModule({
    declarations: [
        ProfessoresPortariaComponent
    ],
    exports: [
        ProfessoresPortariaComponent
    ],
    imports: [
        CommonModule,
        TabelaListagemModule,
        ModalPadraoModule,
        ModalSeletorModule,
        SelectModule
    ]
})
export class ProfessoresPortariaModule { }
