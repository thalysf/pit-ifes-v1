import {CommonModule} from '@angular/common';
import {HttpClientModule} from '@angular/common/http';
import {Component, Input, NgModule, OnChanges, OnDestroy, SimpleChanges, ViewChild} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {DxButtonModule, DxDataGridComponent, DxDataGridModule, DxToolbarModule} from 'devextreme-angular';
import {ApiService} from '../../../services/api.service';
import {ParentResourceSchemaResolve, ResourceSchemaResolve} from '../../../services/resource-schema.resolver';
import {ConfigurationColumn} from "../../../model/resource";
import {ActivatedRoute, Router} from "@angular/router";
import {Observable} from "rxjs";

export interface ActionsTabela {
    text: string,
    icon: string,
    mode?: 'single' | 'batch',
    type: 'success' | 'default' | 'danger',
    hint: string,
    onClick: Function,
    isVisible?: (entity: any) => boolean
}

@Component({
    selector: 'tabela-listagem',
    templateUrl: 'tabela-listagem.component.html',
    styleUrls: [
        '../table-list.component.css',
        '../../modal/tabela-seletor.component.css',
        'tabela-listagem.component.scss',
        '../../formulario-dinamico/formulario-dinamico.component.css'
    ]
})
export class TabelaListagemComponent implements OnDestroy, OnChanges {
    @ViewChild('dataGrid', {static: false}) dataGrid!: DxDataGridComponent;

    @Input() url!: string;
    @Input() colunas!: ConfigurationColumn[];
    @Input() valueExpr!: string;
    @Input() actions: ActionsTabela[] = [];
    @Input() selectionMode: 'single' | 'multiple' = 'single';
    @Input() ativarSelecaoUnica = false;

    colunasListadas: ConfigurationColumn[] = [];
    actionsSingle: ActionsTabela[] = [];
    actionsBatch: ActionsTabela[] = [];
    apiSubscription: any;
    entidades: any[] = [];
    _selectedKeys: string[] = [];

    constructor(
        protected router: Router,
        protected route: ActivatedRoute,
        protected apiService: ApiService
    ) {
    }

    ngOnInit(): void {
    }

    ngOnDestroy(): void {
        if (this.apiSubscription) this.apiSubscription.unsubscribe();
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['colunas']) {
            this.setup();
        }
    }

    searchText = (text: any) => {
        this.search(text.value).subscribe();
    }

    searchEnter = (text: any) => {
        if (text.value) this.search(text.value).subscribe();
    }

    private search(searchText: string) {
        return new Observable(observer => {
            this.dataGrid.instance.searchByText(searchText);
            observer.next();
        })
    }

    private setup() {
        this.entidades = [];
        this.configurarColunas();
        this.configurarIcones();
        this.listarEntidades();
    }

    set selectedKeys(selectedKeys: any[]) {
        this._selectedKeys = selectedKeys;
    }

    get selectedKeys() {
        return this._selectedKeys;
    }

    getSelectedIds = () => {
        return this.dataGrid.instance.getSelectedRowsData().map(data => data[this.valueExpr]);
    }

    getSelectedEntities = () => {
        const idSelecteds = this.getSelectedIds();
        return this.entidades.filter(entidade => idSelecteds.includes(entidade[this.valueExpr]));
    }

    public listarEntidades() {
        this.apiSubscription = this.apiService.get(this.url).subscribe(
            response => {
                this.entidades = response as any[];
            }
        );
    }

    public getEntidades() {
        return this.entidades;
    }

    private configurarColunas() {
        this.colunasListadas = this.colunas.filter(coluna => !coluna.unAvailableOnList).map(coluna => coluna);
    }

    private configurarIcones() {
        this.actionsSingle = this.actions.filter(action => action.mode === 'single');
        this.actionsBatch = this.actions.filter(action => !(action.mode === 'single'));
    }

    showColumnChooser = () => {
        this.dataGrid.instance.showColumnChooser();
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
    ],
    providers: [ApiService, ResourceSchemaResolve, ParentResourceSchemaResolve],
    declarations: [TabelaListagemComponent],
    exports: [TabelaListagemComponent]
})
export class TabelaListagemModule {
}
