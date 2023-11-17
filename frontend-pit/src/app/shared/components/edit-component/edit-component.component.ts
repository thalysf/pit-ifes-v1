import {Component, EventEmitter, Input, NgModule, Output, ViewChild} from '@angular/core';
import {
    FormularioDinamicoComponent,
    FormularioDinamicoModule
} from "../formulario-dinamico/formulario-dinamico.component";
import {ConfigurationColumn, Resource} from "../../model/resource";
import {ActionToolbar, ToolbarModule} from "../toolbar/toolbar.component";
import {ApiService} from "../../services/api.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Observable} from "rxjs";
import notify from "devextreme/ui/notify";
import {confirm} from "devextreme/ui/dialog";

@Component({
    selector: 'app-edit-component',
    templateUrl: './edit-component.component.html',
    styleUrls: ['./edit-component.component.scss']
})
export class EditComponentComponent {
    @ViewChild('formulario', {static: false}) formulario!: FormularioDinamicoComponent;

    @Input() rota!: string;
    @Input() valueExpr!: string;
    @Input() resource!: Resource;
    @Input() id: string | null = null;
    @Input() entidade: any = {};
    @Input() entidadeOriginal: any = {};

    @Output() idChange = new EventEmitter<string | null>();
    @Output() entidadeChange = new EventEmitter<any>();
    @Output() entidadeOriginalChange = new EventEmitter<any>();

    colunas: ConfigurationColumn[] = [];

    actions: ActionToolbar[] = [];


    constructor(private apiService: ApiService, private route: ActivatedRoute, private router: Router) {
        this.actions = [
            {
                type: 'success',
                icon: 'save',
                text: 'Salvar',
                onClick: this.salvar()
            }
        ];
    }

    ngOnInit(): void {
        this.colunas = this.resource.colunas;
        this.id = this.route.snapshot.paramMap.get('id');
        this.idChange.emit(this.id);
        if (this.id) this.getEntidade();
    }

    protected navigateEdit(id: string): void {
        this.router.navigate([this.rota + '/' + id], {replaceUrl: true});
    }

    private getEntidade() {
        if (this.id) {
            this.apiService.detail(this.rota, this.id).subscribe({
                next: (response: any) => {
                    this.entidade = this.tratarEntidade(response);
                    this.entidadeOriginal = response;
                    this.entidadeChange.emit(this.entidade);
                    this.entidadeOriginalChange.emit(this.entidadeOriginal);

                    const newActions = this.actions.map(a => a);
                    newActions.push({
                        icon: 'trash',
                        text: 'Excluir',
                        type: 'danger',
                        onClick: this.excluir()
                    });
                    this.actions = newActions;
                }
            })
        }
    }

    private tratarEntidade(entidade: any) {
        const entidadeTratada: any = {};
        Object.keys(entidade).forEach(key => {
            let value = entidade[key];
            const columnResource: ConfigurationColumn | undefined = this.colunas.find(coluna => coluna.field == key);
            if (columnResource && columnResource.getValueBeforEntity) {
                value = columnResource.getValueBeforEntity(entidade[key]);
            }
            entidadeTratada[key] = value;
        })
        return entidadeTratada;
    }

    private excluir() {
        return new Observable<Object>(observer => {
            confirm('Tem certeza que deseja excluir o item atual?', 'Confirmar Exclusão')
                .then(result => {
                    if(result){
                        this.apiService.delete(this.rota, <string>this!.id).subscribe(
                            (response: any) => {
                                observer.next();
                                if (!this.id) this.navigateEdit(response[this.valueExpr] as string);
                            }, error => {
                                notify(error.error.message, 'error', 4000);
                                observer.error();
                            }
                        );
                    }
                })
        })
    }

    private salvar() {
        return new Observable<Object>(observer => {

            if (!this.formulario.getFormulario().validate().isValid) {
                observer.error();
                return;
            }

            let routeObservable;
            const dadosFormulario = this.formulario.getDadosFormularioSalvar();
            if (!this.id) {
                routeObservable = this.apiService.post(this.rota, dadosFormulario);
            } else {
                routeObservable = this.apiService.put(this.rota, this.id, dadosFormulario);
            }

            routeObservable.subscribe(
                (response: any) => {
                    observer.next();
                    this.entidade = this.tratarEntidade(response);
                    this.entidadeOriginal = response;
                    this.entidadeChange.emit(this.entidade);
                    this.entidadeOriginalChange.emit(this.entidadeOriginal);

                    if (!this.id) this.navigateEdit(response[this.valueExpr] as string);
                    notify('Formulario salvo com sucesso', 'success', 2000);
                }, error => {
                    notify(error.error.message, 'error', 4000);
                    observer.error();
                }
            );
        })
    }
}

@NgModule({
    declarations: [EditComponentComponent],
    imports: [
        ToolbarModule,
        FormularioDinamicoModule
    ],
    exports: [EditComponentComponent]
})
export class EditComponentModule {
}
