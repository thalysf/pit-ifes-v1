import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {AtividadesRoutingModule} from './atividades-routing.module';
import {ApoioEnsinoComponent} from './apoio-ensino/apoio-ensino.component';
import {EditComponentModule} from "../../shared/components/edit-component/edit-component.component";
import {TableAssociacaoModule} from "../../shared/components/table-list/table-associacao/table-associacao.component";
import { PesquisaComponent } from './pesquisa/pesquisa.component';
import { ExtensaoComponent } from './extensao/extensao.component';
import { OutrasComponent } from './outras/outras.component';

import {TabelaListagemModule} from "../../shared/components/table-list/tabela-listagem/tabela-listagem.component";
import {ModalPadraoModule} from "../../shared/components/modal-padrao/modal-padrao.module";
import {
    AssociarProjetosAtividadeComponent
} from "./associar-projetos-atividade/associar-projetos-atividade.component";


@NgModule({
    declarations: [
        ApoioEnsinoComponent,
        PesquisaComponent,
        ExtensaoComponent,
        OutrasComponent,
        AssociarProjetosAtividadeComponent
    ],
    imports: [
        CommonModule,
        AtividadesRoutingModule,
        EditComponentModule,
        TabelaListagemModule,
        TableAssociacaoModule,
        ModalPadraoModule,
    ]
})
export class AtividadesModule {
}
