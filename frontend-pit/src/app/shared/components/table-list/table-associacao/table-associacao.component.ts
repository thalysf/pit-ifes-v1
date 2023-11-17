import {CommonModule} from '@angular/common';
import {HttpClientModule} from '@angular/common/http';
import {Component, Input, NgModule, ViewChild} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {DxButtonModule, DxDataGridModule, DxPopupModule, DxScrollViewModule, DxToolbarModule} from 'devextreme-angular';
import {ApiService} from '../../../services/api.service';
import {ParentResourceSchemaResolve, ResourceSchemaResolve} from '../../../services/resource-schema.resolver';
import {AbstractListModule} from "./../abstract-list.component";
import {Resource} from "../../../model/resource";
import {
    ActionsTabela,
    TabelaListagemComponent,
    TabelaListagemModule
} from "../tabela-listagem/tabela-listagem.component";
import {Observable} from "rxjs";
import {ModalPadraoModule} from "../../modal-padrao/modal-padrao.module";
import {ActivatedRoute} from "@angular/router";


@Component({
    selector: 'table-associacao',
    templateUrl: 'table-associacao.component.html',
    styleUrls: [
        '../table-list.component.css',
        'table-associacao.component.less'
    ]
})
export class TableAssociacaoComponent {

    @ViewChild('tabelaListagem', {static: false}) tabelaListagem!: TabelaListagemComponent;
    @ViewChild('tabelaListagemModal', {static: false}) tabelaListagemModal!: TabelaListagemComponent;
    @Input() resource!: Resource;

    showModal = false;
    selectedKeys: any = [];
    actions: ActionsTabela[] = [];

    protected id: string | null = null;

    constructor(private apiService: ApiService, protected route: ActivatedRoute) {
    }

    ngOnInit(): void {
        this.id = this.route.snapshot.params['id'];

        this.actions = [
            {
                type: 'success',
                icon: 'link',
                mode: 'batch',
                text: '',
                hint: 'Associar ' + this.resource.nomeEntidade.plural,
                onClick: this.abrirModalAssociacao
            },
        ]
    }

    salvar() {
        return new Observable(observer => {
            const selectedIds = this.tabelaListagemModal.getSelectedIds();

            const salvarDto = {
                entidades: selectedIds
            }

            this.apiService.post(this.getUrlOperacoes(), salvarDto).subscribe(response => {
                this.fecharModal();
                observer.next();
            }, () => {
                observer.error();
            })
        })
    }

    protected getUrlOperacoes(): string {
        if (this.resource!.route.targetUrl) return this.resource!.route.targetUrl + '/' + this.id + '/' + this.resource!.route.url;
        return this.resource!.route.url;
    }

    abrirModalAssociacao = () => {
        this.showModal = true;
        this.tabelaListagemModal.listarEntidades();
        this.tabelaListagemModal.selectedKeys = this.tabelaListagem.getEntidades().map(entidade => entidade[this.resource.fieldPk]);
    }

    fecharModal() {
        this.showModal = false;
        this.tabelaListagem.listarEntidades();
    }

    getUrlModalAssociacao() {
        // url do container
        return `${this.resource.route.url}`;
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
        DxPopupModule,
        TabelaListagemModule,
        DxScrollViewModule,
        ModalPadraoModule,
    ],
    providers: [ApiService, ResourceSchemaResolve, ParentResourceSchemaResolve],
    declarations: [TableAssociacaoComponent],
    exports: [TableAssociacaoComponent]
})
export class TableAssociacaoModule {
}
