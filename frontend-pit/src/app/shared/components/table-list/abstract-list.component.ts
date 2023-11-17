import {CommonModule} from '@angular/common';
import {HttpClientModule} from '@angular/common/http';
import {Component, Input, NgModule, OnDestroy, ViewChild} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {ActivatedRoute, Router} from '@angular/router';
import {DxButtonModule, DxDataGridComponent, DxDataGridModule, DxToolbarModule} from 'devextreme-angular';
import {confirm} from 'devextreme/ui/dialog';
import {Resource} from '../../model/resource';
import {ApiService} from '../../services/api.service';
import {ParentResourceSchemaResolve, ResourceSchemaResolve} from '../../services/resource-schema.resolver';
import {
    ActionsTabela,
    TabelaListagemComponent,
    TabelaListagemModule
} from "./tabela-listagem/tabela-listagem.component";
import notify from "devextreme/ui/notify";


@Component({
    selector: 'table-list',
    template: '',
    styleUrls: ['table-list.component.css']
})
export abstract class AbstractListComponent implements OnDestroy {

    @Input() disableNew = false;
    @ViewChild('tabela', {static: false}) tabela!: TabelaListagemComponent;
    @ViewChild(DxDataGridComponent, {static: false}) dataGrid!: DxDataGridComponent;
    resource!: Resource;
    actions: ActionsTabela[] = [];

    routeSubscription: any;
    apiSubscription: any;

    protected id: string | null = null;
    protected entity: string | null = null;
    protected targetEntity: string | null = null;

    constructor(
        protected router: Router,
        protected route: ActivatedRoute,
        protected apiService: ApiService
    ) {
    }

    protected abstract ngOnInit(): void;

    ngOnDestroy(): void {
        if (this.routeSubscription) this.routeSubscription.unsubscribe();
        if (this.apiSubscription) this.apiSubscription.unsubscribe();
    }

    public listarEntidades() {
        this.criarActions();

        this.id = this.route.snapshot.params['id'];
        this.entity = this.route.snapshot.params['entity'];
        this.targetEntity = this.route.snapshot.params['targetEntity'];
    }

    protected criarActions(){
        this.actions = [
            {
                type: 'success',
                icon: 'plus',
                mode: 'batch',
                text: '',
                hint: 'Adicionar',
                onClick: this.onClickNovo
            },
            {
                mode: 'single',
                text: '',
                hint: 'Editar ' + this.resource.nomeEntidade.singular,
                icon: 'edit',
                type: 'success',
                onClick: this.editar
            },
            {
                mode: 'single',
                text: '',
                hint: 'Excluir ' + this.resource.nomeEntidade.singular,
                icon: 'trash',
                type: 'success',
                onClick: this.excluir
            }
        ];

        this.resource.actions?.forEach(action => {
            this.actions.push({
                ...action,
                onClick: () => action.onClick(this.apiService, this.router, this.route)
            })
        })
    }

    public editar = (evt: any) => {
        this.navigateEdit(evt);
    }

    public onClickNovo = () => {
        this.navigateNew();
    }

    public excluir = (evt: any) => {
        const entidade = evt.row.data;
        confirm('Tem certeza que deseja excluir? Não será possível desfazer a alteração.', 'Confirmar exclusão').then((confirmExcluir) => {
            if (confirmExcluir) {
                const idEntidade = entidade[this.resource!.fieldPk];
                this.apiService.delete(this.getUrlOperacoes(), idEntidade).subscribe(
                    () => {
                        this.tabela.listarEntidades();
                    }, e => {
                        notify(e.error.message, 'error', 4000);
                    }
                );
            }
        });
    }

    showColumnChooser() {
        this.dataGrid.instance.showColumnChooser();
    }

    protected abstract getUrlOperacoes(): string;

    protected abstract navigateNew(): void;

    protected abstract navigateEdit(evt: any): void;
}

@NgModule({
    imports: [
        CommonModule,
        BrowserModule,
        DxButtonModule,
        DxDataGridModule,
        HttpClientModule,
        DxToolbarModule
    ],
    providers: [ApiService, ResourceSchemaResolve, ParentResourceSchemaResolve]
})
export class AbstractListModule {
}
