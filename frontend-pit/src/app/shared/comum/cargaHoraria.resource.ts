import {ColumnDataType, ConfigurationColumn} from "../model/resource";
import {HoraConfiguration} from "../components/carga-horaria-editor/carga-horaria-editor.component";

export const cargaHorariaResourceColumn: ConfigurationColumn[] = [
    {
        field: 'cargaHorariaMinima',
        label: 'Carga Horaria Min.',
        required: true,
        type: ColumnDataType.CargaHoraria,
        getValueBeforEntity: (double: number) => doubleToCargaHorariaComponent(double),
        getValueBeforSave: (carga: HoraConfiguration) => stringHourFormatToDouble(carga.id),
        calculateDisplayValue: (entity: any) => doubleToDateStringFormat(entity.cargaHorariaMinima)
    },
    {
        field: 'cargaHorariaMaxima',
        label: 'Carga Horaria Max.',
        required: true,
        type: ColumnDataType.CargaHoraria,
        getValueBeforEntity: (double: number) => doubleToCargaHorariaComponent(double),
        getValueBeforSave: (carga: HoraConfiguration) => stringHourFormatToDouble(carga.id),
        calculateDisplayValue: (entity: any) => doubleToDateStringFormat(entity.cargaHorariaMaxima)
    },
]

export const dataToDouble = (data: Date) => {
    const hora = data.getHours();
    const minutos = data.getMinutes();
    return hora + (minutos / 100);
}

export const doubleToDate = (double: number) => {
    const hora = Math.floor(double);
    const minutos = Math.ceil(parseFloat((double - hora).toFixed(2)) * 100);
    const data = new Date();
    data.setHours(hora);
    data.setMinutes(minutos);
    data.setSeconds(0);
    return data;
}

export const doubleToDateStringFormat = (double: number) => {
    const hora = Math.floor(double);
    const minutos = Math.ceil(parseFloat((double - hora).toFixed(2)) * 100);

    const stringHora = hora.toString().padStart(2, '0');
    const stringMinutos = minutos.toString().padStart(2, '0');

    return `${stringHora}:${stringMinutos}`;
}

export const stringHourFormatToDouble = (text: string) => {
    const [horaString, minutoString] = text.split(':');
    const hora = Number(horaString);
    const minutos = Number(minutoString);
    return hora + (minutos / 100);
}

export const doubleToCargaHorariaComponent = (double: number) : HoraConfiguration => {
    const time = doubleToDateStringFormat(double);
    return {id: time, nome: time};
}

export const incrementHora = (double: number, increment: number): number =>{
    double += increment;

    let horas = Math.floor(double);
    let minutos = Math.ceil(parseFloat((double - horas).toFixed(2)) * 100);

    if (minutos > 59) {
        horas += 1;
        minutos = minutos - 60;
    }
    return horas + (minutos / 100);
}
