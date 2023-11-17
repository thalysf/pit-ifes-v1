import {NgModule} from '@angular/core';
import {ProjetoComponent} from "./projeto.component";
import {EditComponentModule} from "../../shared/components/edit-component/edit-component.component";
import {ProfessoresProjetoModule} from "./professores-projeto/professores-projeto.module";
import {CommonModule} from "@angular/common";
import {ProjetoRoute} from "./projeto.route";
import {PortariaModule} from "../portaria/portaria.module";
import {AtividadeAssociadaModule} from "../portaria/atividade-associada/atividade-associada.component";
import { ImportarProjetosComponent } from './importar-projetos/importar-projetos.component';
import {ImportarModule} from "../../shared/components/importar/importar.component";


@NgModule({
    declarations: [
        ProjetoComponent,
        ImportarProjetosComponent
    ],
    imports: [
        CommonModule,
        ProjetoRoute,
        EditComponentModule,
        ProfessoresProjetoModule,
        PortariaModule,
        AtividadeAssociadaModule,
        ImportarModule
    ]
})
export class ProjetoModule {
}
