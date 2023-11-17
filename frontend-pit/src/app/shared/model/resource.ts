import {ActionsTabela} from "../components/table-list/tabela-listagem/tabela-listagem.component";

export enum ColumnDataType{
    Checkbox = 'dxCheckBox',
    Select = 'dxSelectbox',
    Date = 'dxDateBox',
    Number = 'dxNumberBox',
    Text = 'dxTextBox',
    Lookup = 'lookup',
    CargaHoraria = 'cargaHoraria'

    // 'dxAutocomplete' | 'dxCalendar' | 'dxColorBox' | 'dxDropDownBox' | 'dxHtmlEditor'
    // 'dxLookup' | 'dxRadioGroup' | 'dxRangeSlider' | 'dxSlider' | 'dxSwitch' | 'dxTagBox'
    // 'dxTextArea'
}



export interface ColumnValidation {
    type: 'email' | 'compare' | 'pattern' | 'range' | 'numeric',
    message?: string
}

export interface LookupConfiguration{
    colunas: ConfigurationColumn[],
    route: string,
    fieldPk: string,
    fieldName: string
}

export interface SelectConfiguration{
    displayExpr: string,
    valueExpr: string,
    readOnly?: boolean,
    defaultData?: any[],
    route?: string
}

export interface EditorOptions {
    items: any[],
    searchEnabled?: boolean,
    value: string
}

export interface ConfigurationColumn{
    field: string,
    label?: string,
    visible?: boolean,

    type?: ColumnDataType,
    validation?: ColumnValidation[],
    required?: boolean,
    colSpan?: number,
    dateType?: 'date' | 'datetime' | string,
    pickerType?: 'calendar' | 'list' | 'native' | 'rollers',
    lookupConfiguration?: LookupConfiguration,
    selectConfiguration?: SelectConfiguration,
    editorOptions?: EditorOptions,
    defaultValue?: any,
    calculateDisplayValue?: Function,
    mask?: string,
    width?: number,

    getValueBeforEntity?: (value: any) => any,
    getValueBeforSave?: (value: any) => any
    visibleForm?: (entity: any) => boolean,
    unAvailableOnList?: boolean,
}

export interface Resource {
    nomeEntidade: {
        singular: string,
        plural: string,
    },
    /**
     *Rota da api que fara as operações de consulta, etc
     */
    route: {
        url: string,
        targetUrl?: string
    },
    /**
     * Nome do campo que referencia o id (pk) da entidade.
    */
    fieldPk: string,
    colunas: ConfigurationColumn[],
    availableReferences: AvailableReference[],
    disableBefore?: boolean,
    disableDelete?: boolean,

    actions?: ActionsTabela[]
}

export interface AvailableReference extends Resource{
    tipoAssociacao:  'OneToMany' | 'ManyToOne' | 'ManyToMany',
    isVisible?: (entity: any) => boolean
}
