import {CommonModule} from '@angular/common';
import {HttpClientModule} from '@angular/common/http';
import {Component, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {DxButtonModule, DxDataGridModule, DxToolbarModule} from 'devextreme-angular';
import {ApiService} from '../../services/api.service';
import {ParentResourceSchemaResolve, ResourceSchemaResolve} from '../../services/resource-schema.resolver';
import {AbstractListComponent} from "./abstract-list.component";
import {TabelaListagemModule} from "./tabela-listagem/tabela-listagem.component";


@Component({
    selector: 'base-list',
    templateUrl: 'table-list.component.html',
    styleUrls: ['table-list.component.css']
})
export class BaseListComponent extends AbstractListComponent {

    ngOnInit(): void {
        this.routeSubscription = this.route.data.subscribe(data => {
            this.resource = data['schema'];
            this.listarEntidades();
        });
    }

    protected getUrlOperacoes(): string {
        return this.resource.route.url;
    }

    protected navigateNew(): void {
        this.router.navigate(['novo'], {relativeTo: this.route});
    }

    protected navigateEdit(evt: any): void {
        const idEntidade = evt.row.data[this.resource.fieldPk];
        this.router.navigate([idEntidade], {relativeTo: this.route});
    }
}

@NgModule({
    imports: [
        CommonModule,
        BrowserModule,
        DxButtonModule,
        DxDataGridModule,
        HttpClientModule,
        DxToolbarModule,
        TabelaListagemModule
    ],
    providers: [ApiService, ResourceSchemaResolve, ParentResourceSchemaResolve],
    declarations: [BaseListComponent],
    exports: [BaseListComponent]
})
export class BaseListModule {
}
