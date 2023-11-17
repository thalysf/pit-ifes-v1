import {
    Component,
    EventEmitter,
    Input,
    NgModule,
    OnDestroy,
    OnInit,
    Output,
    SimpleChanges,
    ViewChild
} from '@angular/core';
import {CommonModule, Location} from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {
    DxButtonModule,
    DxFormComponent,
    DxFormModule,
    DxSelectBoxModule,
    DxTextBoxModule,
    DxToolbarModule
} from 'devextreme-angular';
import {ColumnDataType, ConfigurationColumn, EditorOptions} from '../../model/resource';
import {ApiService} from '../../services/api.service';
import {ActivatedRoute, Router} from '@angular/router';
import {TableListModule} from "../table-list/table-list.component";
import {SelectModule} from "../select/select.component";
import {DxoSelectionModule} from "devextreme-angular/ui/nested";
import DataSource from "devextreme/data/data_source";
import {Item} from 'devextreme/ui/form';
import {TableAssociacaoModule} from "../table-list/table-associacao/table-associacao.component";
import {CargaHorariaEditorModule} from "../carga-horaria-editor/carga-horaria-editor.component";


@Component({
    selector: 'formulario-dinamico',
    templateUrl: 'formulario-dinamico.component.html',
    styleUrls: ['formulario-dinamico.component.css']
})
export class FormularioDinamicoComponent implements OnInit, OnDestroy {
    @ViewChild('formulario', {static: false}) private formulario!: DxFormComponent;

    @Input() entidade: any = {};
    @Input() colunas!: ConfigurationColumn[];
    @Input() labelLocation = 'left';

    @Output() entidadeChange: EventEmitter<any> = new EventEmitter<any>();

    // @ViewChild('container', {static: true}) private container!: ElementRef;


    // private formulario!: Form;

    columnEnum = ColumnDataType;

    routeSubscription: any;
    apiSubscription: any;
    itemsForm!: Item[];

    constructor(
        private location: Location,
        private route: ActivatedRoute,
        private router: Router,
        private apiService: ApiService
    ) {
        if (window.innerWidth <= 580) {
            this.labelLocation = 'top';
        }
    }

    ngOnInit(): void {
        this.setup();
    }

    ngOnDestroy(): void {
        if (this.routeSubscription) this.routeSubscription.unsubscribe();
        if (this.apiSubscription) this.apiSubscription.unsubscribe();
        // if (this.formulario) this.formulario.dispose();
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['resource'] && changes['resource'].currentValue)
            this.setup();

        if (changes['entidade']) {
            this.setup();
        }
    }

    setup = () => {
        this.itemsForm = this.getItems();
    }

    selectedItemModalChanged = (changed: any, column: ConfigurationColumn) => {
        const stringPkEntidade: string = column.lookupConfiguration!.fieldPk;
        const codigoEntidade: string = changed[stringPkEntidade as string];

        if (!this.entidade) this.entidade = {};
        this.entidade[column.field] = codigoEntidade;
    }

    getEditorOptions(editorOptions: EditorOptions): EditorOptions {
        return {
            items: editorOptions.items,
            searchEnabled: editorOptions.searchEnabled ? editorOptions.searchEnabled : true,
            value: editorOptions.value
        }
    }

    // private createForm(): Form {
    //     const options: Options = {
    //         // readOnly: false,
    //         // showColonAfterLabel: true,
    //         labelLocation: this.labelLocation as LabelLocation,
    //         formData: this.entidade,
    //         // minColWidth: 300,
    //         colCount: 1,
    //         items: this.getItems()
    //     };
    //
    //     return new Form(this.container.nativeElement, options);
    // }

    public getItems(): Item[] {

        return this.colunas
            // .filter(coluna => this.isColunaVisible(coluna))
            .map(coluna => ({
                dataField: coluna.field,
                visible: this.isColunaVisible(coluna),
                template: coluna.type == ColumnDataType.CargaHoraria ? 'cargaHoraria' : undefined,
                label: {text: coluna.label},
                validationRules: coluna.required ? [{type: 'required', message: 'Campo obrigatório'}] : undefined,
                ...this.getEditor(coluna),
            } as Item));
    }

    private getEditor(column: ConfigurationColumn): Item {
        switch (column.type) {
            case ColumnDataType.Text:
                return {editorType: 'dxTextBox', editorOptions: {showClearButton: true, mask: column.mask, width: column.width}};
            // case ColumnDataType.Password:
            //     return {editorType: 'dxTextBox', editorOptions: {mode: 'password'}};
            case ColumnDataType.Number:
                return {editorType: 'dxNumberBox', editorOptions: {showClearButton: true}};
            // case ColumnDataType.Decimal:
            //     return {editorType: 'dxNumberBox', editorOptions: {showClearButton: true}};
            // case ColumnDataType.Textarea:
            //     return {editorType: 'dxTextBox', editorOptions: {showClearButton: true}};
            case ColumnDataType.Checkbox:
                return {editorType: 'dxCheckBox'};
            case ColumnDataType.Date:
                return {editorType: 'dxDateBox', editorOptions: {
                    showClearButton: true, width: 250, type: column.dateType,
                        value: column.defaultValue
                }};
            case ColumnDataType.Select:
                return {editorType: 'dxSelectBox', editorOptions: this.getSelectOptions(column)};
            // case ColumnDataType.SelectMany:
            //     return {editorType: 'dxTagBox', editorOptions: this.getTagOptions(field)};
            // case ColumnDataType.Lookup:
            //     return {editorType: 'dxTagBox', editorOptions: this.getTagOptions(field)};
        }

        return {};
    }

    private getSelectOptions(column: ConfigurationColumn): any {
        if (!column.selectConfiguration)
            throw new Error(`Propriedade selectConfiguration esta faltando no campo ${column.field}`);
        return {
            dataSource: this.createDataSource(column),
            displayExpr: column.selectConfiguration.displayExpr,
            valueExpr: column.selectConfiguration.valueExpr,
            showClearButton: true,
        };
    }

    private getTagOptions(field: any): any {
        return this.getSelectOptions(field);
    }

    private createDataSource(column: ConfigurationColumn): DataSource | any[] {
        if (column.selectConfiguration?.route) {
            return new DataSource({
                loadMode: 'raw',
                paginate: false,
                key: column.field,
                load: () => {
                    return new Promise<any>((resolve, reject) => {
                        // @ts-ignore
                        this.apiService.get(column.selectConfiguration!.route).subscribe(response => {
                            resolve(response);
                        }, error => {
                            reject(error);
                        })
                    })
                }
            });
        } else {
            // @ts-ignore
            return column.selectConfiguration!.defaultData;
        }
    }

    /**
     * Retorna o objeto de formulario do devextreme
     */
    public getFormulario() {
        return this.formulario.instance;
    }

    /**
     * Retorna se o formulario é valido e marca os campos invalidos com a formatação
     */
    public isValid() {
        return this.formulario.instance.validate().isValid;
    }

    /**
     * Retorna os dados serializados do formulario, já tratados caso haja função de tratamento para salvar
     */
    public getDadosFormularioSalvar() {
        const formData: any = {};

        Object.entries(this.formulario.formData).forEach(([key, value]) => {
            let valueSave = value;

            const columnResource: ConfigurationColumn | undefined = this.colunas.find(coluna => coluna.field == key);
            if(columnResource && columnResource.getValueBeforSave){
                valueSave = columnResource.getValueBeforSave(value);
            }

            formData[key] = valueSave;
        });

        return formData;
    }

    private isColunaVisible(coluna: ConfigurationColumn): boolean {
        if (coluna.visibleForm === undefined) return true;
        return coluna.visibleForm(this.entidade);
    }

    formularioChanged($event: any) {
        this.checarVisibilidadeCampos();
    }

    private checarVisibilidadeCampos() {
        this.colunas.filter(coluna => coluna.visibleForm).forEach(coluna => {
            const isVisible = coluna.visibleForm!(this.entidade);

            this.formulario.instance.itemOption(coluna.field, "visible", isVisible);
        })
    }

    log(data: any) {
        console.log(data);
        return 'teste';
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
        DxTextBoxModule,
        CargaHorariaEditorModule
    ],
    declarations: [FormularioDinamicoComponent],
    exports: [FormularioDinamicoComponent]
})
export class FormularioDinamicoModule {
}
