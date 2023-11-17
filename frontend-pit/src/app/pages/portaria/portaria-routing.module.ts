import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthGuardService} from "../../shared/services";
import {PortariaComponent} from "./portaria.component";
import {ImportarPortariaComponent} from "./importar-portaria/importar-portaria.component";

const routes: Routes = [
    {
        path: 'portarias/importar',
        component: ImportarPortariaComponent,
        canActivate: [AuthGuardService]
    },
    {
        path: 'portarias/novo',
        component: PortariaComponent,
        canActivate: [AuthGuardService]
    },
    {
        path: 'portarias/:id',
        component: PortariaComponent,
        canActivate: [AuthGuardService]
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class PortariaRoutingModule {
}
