import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthGuardService} from "../../shared/services";
import {PitComponent} from "./pit.component";
import {FormularioPitComponent} from "./formulario-pit/formulario-pit.component";
import {ExecucaoPitComponent} from "./execucao-pit/execucao-pit.component";
import {ListagemPitsCoordenadorComponent} from "./listagem-pits-coordenador/listagem-pits-coordenador.component";
import {RevisaoPitComponent} from "./listagem-pits-coordenador/revisao-pit/revisao-pit.component";


const routes: Routes = [
    {
        path: 'pits',
        component: PitComponent,
        canActivate: [AuthGuardService]
    },
    {
        path: 'pits/emRevisao',
        component: ListagemPitsCoordenadorComponent,
        canActivate: [AuthGuardService]
    },
    {
        path: 'pits/aprovado',
        component: ListagemPitsCoordenadorComponent,
        canActivate: [AuthGuardService]
    },
    {
        path: 'pits/new',
        component: FormularioPitComponent,
        canActivate: [AuthGuardService]
    },
    {
        path: 'pits/responder/:id',
        component: ExecucaoPitComponent,
        canActivate: [AuthGuardService]
    },
    {
        path: 'pits/revisar/:id',
        component: RevisaoPitComponent,
        canActivate: [AuthGuardService]
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    providers: [AuthGuardService],
    exports: [RouterModule]
})
export class PitRoute {
}
