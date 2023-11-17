import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthGuardService} from "../../shared/services";
import {CoordenadoresComponent} from "./coordenadores.component";

const routes: Routes = [
    {
        path: 'coordenadores/novo',
        component: CoordenadoresComponent,
        canActivate: [AuthGuardService]
    },
    {
        path: 'coordenadores/:id',
        component: CoordenadoresComponent,
        canActivate: [AuthGuardService]
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    providers: [AuthGuardService],
    exports: [RouterModule]
})
export class CoordenadoresRoute {
}
