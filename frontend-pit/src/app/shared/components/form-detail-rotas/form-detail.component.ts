import {Component, Input, NgModule, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {CommonModule, Location} from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {DxButtonModule, DxFormModule, DxSelectBoxModule, DxToolbarModule} from 'devextreme-angular';
import {ColumnDataType, ConfigurationColumn, Resource} from '../../model/resource';
import {ApiService} from '../../services/api.service';
import {ActivatedRoute, Router} from '@angular/router';
import {confirm} from "devextreme/ui/dialog"
import {TableListModule} from "../table-list/table-list.component";
import {SelectModule} from "../select/select.component";
import {DxoSelectionModule} from "devextreme-angular/ui/nested";
import {TableAssociacaoModule} from "../table-list/table-associacao/table-associacao.component";
import {ActionToolbar, ToolbarModule} from "../toolbar/toolbar.component";
import {Observable} from "rxjs";
import {
    FormularioDinamicoComponent,
    FormularioDinamicoModule
} from "../formulario-dinamico/formulario-dinamico.component";
import notify from "devextreme/ui/notify";


@Component({
    selector: 'form-detail',
    templateUrl: 'form-detail.component.html',
})
export class FormDetailComponent implements OnInit, OnDestroy {
    @Input() entidade: any = {};
    @Input() resource!: Resource;

    @ViewChild('formulario', {static: false}) private formulario!: FormularioDinamicoComponent;

    private id: string | null = null;
    private entity: string | null = null;
    private targetId: string | null = null;
    private targetEntity: string | null = null;
    actions: ActionToolbar[] = [];

    columnEnum = ColumnDataType;
    labelLocation = 'left';
    routeSubscription: any;
    apiSubscription: any;

    constructor(
        private location: Location,
        private route: ActivatedRoute,
        private router: Router,
        private apiService: ApiService
    ) {
        if (window.innerWidth <= 580) {
            this.labelLocation = 'top';
        }

        this.actions = [
            {
                type: 'success',
                icon: 'save',
                text: 'Salvar',
                onClick: this.salvar()
            }
        ]
    }

    ngOnInit(): void {
        if (!this.possuiEntidade()) this.routeSubscription = this.route.data.subscribe(data => {
            this.resource = data['schema'];
            this.detalharEntidade();
        });
    }

    ngOnDestroy(): void {
        if (this.routeSubscription) this.routeSubscription.unsubscribe();
        if (this.apiSubscription) this.apiSubscription.unsubscribe();
    }

    private detalharEntidade() {
        this.id = this.route.snapshot.params['id'];
        this.entity = this.route.snapshot.params['entity'];
        this.targetId = this.route.snapshot.params['targetId'];
        this.targetEntity = this.route.snapshot.params['targetEntity'];

        let idDetail = null;
        if (this.id && !this.targetEntity) {
            idDetail = this.id;
        } else if (this.targetId) {
            idDetail = this.targetId;
        }

        if (idDetail) {
            this.apiSubscription = this.apiService.detail(this.getUrlOperacoes(), idDetail).subscribe(
                response => {
                    this.entidade = this.tratarEntidade(response);

                    const newActions = this.actions.slice()
                    newActions.push({
                        icon: 'trash',
                        text: 'Excluir',
                        type: 'danger',
                        onClick: this.excluir()
                    });
                    this.actions = newActions;
                }
            );
        }
    }

    private tratarEntidade(entidade: any){
        const entidadeTratada: any = {};
        Object.keys(entidade).forEach(key => {
            let value = entidade[key];
            const columnResource: ConfigurationColumn | undefined = this.resource.colunas.find(coluna => coluna.field == key);
            if(columnResource && columnResource.getValueBeforEntity){
                value = columnResource.getValueBeforEntity(entidade[key]);
            }
            entidadeTratada[key] = value;
        })
        return entidadeTratada;
    }

    public back() {
        this.location.back();
    }

    salvar = () => {
        return new Observable<Object>(observer => {
            if (!this.formulario) {
                return observer.error();
            }

            if (this.formulario.isValid()) {
                const entitySave = this.formulario.getDadosFormularioSalvar();

                let request;

                if (this.possuiEntidade()) {
                    const idEntidade = this.entidade[this.resource.fieldPk];
                    request = this.apiService.put(this.getUrlOperacoes(), idEntidade, entitySave);
                } else {
                    request = this.apiService.post(this.getUrlOperacoes(), entitySave);
                }

                request.subscribe((response: any) => {
                    observer.next();
                    notify('Formulario salvo com sucesso', 'success', 2000);
                    this.navigateEdit(response[this.resource.fieldPk]);
                }, error => {
                    notify(error.error.message, 'error', 4000);
                    observer.error(error);
                })
            } else {
                return observer.error('Formulario inválido');
            }
        })
    }

    public excluir() {
        return new Observable<Object>(observer => {
            confirm('Tem certeza que deseja excluir? Não será possível desfazer a alteração.', 'Confirmar exclusão').then((confirmExcluir) => {
                if (confirmExcluir) {
                    const idEntidade = this.entidade[this.resource.fieldPk];
                    this.apiService.delete(this.getUrlOperacoes(), idEntidade).subscribe(
                        () => {
                            observer.next()
                            this.back();
                        }, (error) => {
                            observer.error(error);
                        }
                    );
                } else {
                    observer.next();
                }
            });
        });
    }

    public possuiEntidade() {
        return this.entidade != null && this.entidade[this.resource?.fieldPk] != undefined;
    }

    private getUrlOperacoes(): string {
        if (this.targetEntity) return this.resource.route.targetUrl + '/' + this.id + '/' + this.resource.route.url;
        return this.resource.route.url;
    }

    private navigateEdit(id: string): void {
        const route = this.getUrlOperacoes() + '/' + id;
        this.router.navigate([route], {replaceUrl: true});
    }

    isVisible(isVisible?: (entity: any) => boolean) {
        if(isVisible == null || isVisible == undefined){
            return true;
        } else {
            return isVisible(this.entidade);
        }
    }
}

@NgModule({
    imports: [
        CommonModule,
        BrowserModule,
        DxFormModule,
        DxToolbarModule,
        DxButtonModule,
        TableListModule,
        DxSelectBoxModule,
        DxoSelectionModule,
        SelectModule,
        TableAssociacaoModule,
        ToolbarModule,
        FormularioDinamicoModule
    ],
    declarations: [FormDetailComponent],
    exports: [FormDetailComponent]
})
export class FormDetailModule {
}
