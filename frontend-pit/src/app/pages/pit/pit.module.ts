import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PitComponent} from './pit.component';
import {PitRoute} from "./pit.route";
import {TabelaListagemModule} from "../../shared/components/table-list/tabela-listagem/tabela-listagem.component";
import {FormularioPitComponent} from './formulario-pit/formulario-pit.component';
import {BrowserModule} from "@angular/platform-browser";
import {ToolbarModule} from "../../shared/components/toolbar/toolbar.component";
import {
    DxButtonModule,
    DxDateBoxModule, DxLoadIndicatorModule,
    DxLoadPanelModule,
    DxNumberBoxModule,
    DxRadioGroupModule,
    DxTabsModule, DxTextAreaModule,
    DxTextBoxModule, DxToolbarModule
} from "devextreme-angular";
import {ExecucaoPitComponent} from "./execucao-pit/execucao-pit.component";
import {ResponderAulasComponent} from './responder-aulas/responder-aulas.component';
import {ModalSeletorModule} from "../../shared/components/modal/modal-seletor.component";
import {ResponderApoioEnsinoComponent} from './responder-apoio-ensino/responder-apoio-ensino.component';
import {DetalhamentoProjetosComponent} from './detalhamento-projetos/detalhamento-projetos.component';
import {SeletorUnicoModule} from "../../shared/components/modal/seletor-unico/seletor-unico";
import {ModalPadraoModule} from "../../shared/components/modal-padrao/modal-padrao.module";
import {DetalhamentoPortariasComponent} from './detalhamento-portarias/detalhamento-portarias.component';
import { DetalhamentoAulasComponent } from './detalhamento-aulas/detalhamento-aulas.component';
import { DetalhamentoAlunosComponent } from './detalhamento-alunos/detalhamento-alunos.component';
import {FormularioDinamicoModule} from "../../shared/components/formulario-dinamico/formulario-dinamico.component";
import { ResponderMediacaoPedagogicaComponent } from './responder-mediacao-pedagogica/responder-mediacao-pedagogica.component';
import { ResponderPesquisaComponent } from './responder-pesquisa/responder-pesquisa.component';
import { ResponderExtensaoComponent } from './responder-extensao/responder-extensao.component';
import { ResponderGestaoComponent } from './responder-gestao/responder-gestao.component';
import { ResponderRepresentacaoComponent } from './responder-representacao/responder-representacao.component';
import { ResponderOutrasComponent } from './responder-outras/responder-outras.component';
import { ListagemPitsCoordenadorComponent } from './listagem-pits-coordenador/listagem-pits-coordenador.component';
import { RevisaoPitComponent } from './listagem-pits-coordenador/revisao-pit/revisao-pit.component';
import {CargaHorariaEditorModule} from "../../shared/components/carga-horaria-editor/carga-horaria-editor.component";


@NgModule({
    declarations: [
        PitComponent,
        FormularioPitComponent,
        ExecucaoPitComponent,
        ResponderAulasComponent,
        ResponderApoioEnsinoComponent,
        DetalhamentoProjetosComponent,
        DetalhamentoPortariasComponent,
        DetalhamentoAulasComponent,
        DetalhamentoAlunosComponent,
        ResponderMediacaoPedagogicaComponent,
        ResponderPesquisaComponent,
        ResponderExtensaoComponent,
        ResponderGestaoComponent,
        ResponderRepresentacaoComponent,
        ResponderOutrasComponent,
        ListagemPitsCoordenadorComponent,
        RevisaoPitComponent
    ],
    imports: [
        CommonModule,
        BrowserModule,
        PitRoute,
        TabelaListagemModule,
        ToolbarModule,
        DxRadioGroupModule,
        DxDateBoxModule,
        DxLoadPanelModule,
        DxTabsModule,
        ModalSeletorModule,
        DxNumberBoxModule,
        DxButtonModule,
        DxTextBoxModule,
        SeletorUnicoModule,
        ModalPadraoModule,
        FormularioDinamicoModule,
        DxLoadIndicatorModule,
        DxToolbarModule,
        DxTextAreaModule,
        CargaHorariaEditorModule,
    ]
})
export class PitModule {
}
