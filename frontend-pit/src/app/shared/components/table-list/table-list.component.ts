import {CommonModule} from '@angular/common';
import {HttpClientModule} from '@angular/common/http';
import {Component, Input, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {DxButtonModule, DxDataGridModule, DxToolbarModule} from 'devextreme-angular';
import {ApiService} from '../../services/api.service';
import {ParentResourceSchemaResolve, ResourceSchemaResolve} from '../../services/resource-schema.resolver';
import {AbstractListComponent, AbstractListModule} from "./abstract-list.component";
import {Resource} from "../../model/resource";
import {TabelaListagemModule} from "./tabela-listagem/tabela-listagem.component";


@Component({
    selector: 'table-list',
    templateUrl: 'table-list.component.html',
    styleUrls: ['table-list.component.css']
})
export class TableListComponent extends AbstractListComponent {

    @Input() resourceInput!: Resource;

    ngOnInit(): void {
        this.resource = this.resourceInput;
        this.listarEntidades();
    }

    protected getUrlOperacoes(): string {
        if (this.resource!.route.targetUrl) return this.resource!.route.targetUrl + '/' + this.id + '/' + this.resource!.route.url;
        return this.resource!.route.url;
    }

    protected navigateEdit(evt: any): void {
        const idEntidade = evt.row.data[this.resource.fieldPk];
        const route = this.resource.route.url + '/' + idEntidade;
        this.router.navigate([route], {relativeTo: this.route});
    }

    protected navigateNew(): void {
        const route = this.resource.route.url + '/novo';
        this.router.navigate([route], {relativeTo: this.route});
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
        AbstractListModule,
        TabelaListagemModule
    ],
    providers: [ApiService, ResourceSchemaResolve, ParentResourceSchemaResolve],
    declarations: [TableListComponent],
    exports: [TableListComponent]
})
export class TableListModule {
}
