import {Component, Input, OnInit} from '@angular/core';
import {ConfigurationColumn} from "../../../shared/model/resource";
import {ApiService} from "../../../shared/services/api.service";
import {ComponenteCurricular} from "../../../shared/model/componenteCurricular";
import {doubleToCargaHorariaComponent, stringHourFormatToDouble} from "../../../shared/comum/cargaHoraria.resource";
import notify from "devextreme/ui/notify";
import {confirm} from "devextreme/ui/dialog";
import {Observable} from "rxjs";
import {HoraConfiguration} from "../../../shared/components/carga-horaria-editor/carga-horaria-editor.component";

interface RespostaAula {
    componenteCurricular: ComponenteCurricular,
    cargaHorariaSemanal: number,
    idDetalhamentoComponenteCurricular: string,
}

interface RespostaAulaComponente {
    componenteCurricular?: ComponenteCurricular,
    cargaHorariaSemanal: HoraConfiguration,
    idDetalhamentoComponenteCurricular?: string,
}

@Component({
    selector: 'app-detalhamento-aulas',
    templateUrl: './detalhamento-aulas.component.html',
    styleUrls: ['./detalhamento-aulas.component.scss']
})
export class DetalhamentoAulasComponent implements OnInit {

    @Input() nomeAtividade!: string;
    @Input() idAtividade!: string;
    @Input() idPit!: string;
    @Input() totalHoras!: HoraConfiguration;
    @Input() obterTotalHoras!: Function;
    @Input() pitAprovadoOuEmRevisao!: boolean;
    @Input() min!: number;
    @Input() max!: number;

    showModalDetalhamento = false;
    colunasComponentesCurriculares: ConfigurationColumn[];
    colunasFilter = ['nome_curso', 'nome'];
    rotaComponentes = '';

    respostas: RespostaAulaComponente[] = [];

    constructor(private apiService: ApiService) {
        this.colunasComponentesCurriculares = [
            {
                field: 'curso.nome',
                label: 'Curso',
            },
            {
                field: 'nome',
                label: 'Componente curricular',
            }
        ]
    }

    ngOnInit(): void {
        this.rotaComponentes = `respostas/apoioEnsino/componentes_nao_detalhados?idPit=${this.idPit}&idAtividade=${this.idAtividade}`;
    }

    ngOnDestroy(): void {

    }

    onClickRemoverLinha(resposta: RespostaAulaComponente) {
        confirm('Tem certeza que deseja remover o Componente Curricular selecionado?', 'Confirmar Exclusão')
            .then(confirmado => {
                if (confirmado) {
                    this.removerLinha(resposta);
                }
            })
    }

    removerLinha(resposta: RespostaAulaComponente) {
        this.respostas.splice(this.respostas.indexOf(resposta), 1);
        this.salvar(false)
            .subscribe();
    }

    verificarArrayRespostas() {
        if (this.respostas.length == 0 || this.respostas[this.respostas.length - 1]?.componenteCurricular) {
            this.criarRespostaVazia();
        }
    }

    formatNomeComponente(componente: ComponenteCurricular) {
        return `${componente?.nome} (${componente?.curso?.nome})`;
    }

    private listarRespostaAulas() {
        this.apiService.get('respostas/apoioEnsino/aulas', {
            idAtividade: this.idAtividade,
            idPit: this.idPit
        }).subscribe((response: any) => {
            const detalhamentos = response as RespostaAula[];

            this.respostas = [];

            detalhamentos.forEach(detalhamento => {
                this.respostas.push({
                    cargaHorariaSemanal: doubleToCargaHorariaComponent(detalhamento.cargaHorariaSemanal),
                    componenteCurricular: detalhamento.componenteCurricular,
                    idDetalhamentoComponenteCurricular: detalhamento.idDetalhamentoComponenteCurricular,
                })
            })

            this.verificarArrayRespostas();
        })
    }

    private criarRespostaVazia() {
        this.respostas.push({
            cargaHorariaSemanal: doubleToCargaHorariaComponent(0),
        });
    }

    private getDetalhamentos() {
        return this.respostas.filter(resposta => resposta.componenteCurricular != null).map(respostaAula => {
            return {
                cargaHorariaSemanal: stringHourFormatToDouble(respostaAula.cargaHorariaSemanal.id),
                componenteCurricular: {idComponenteCurricular: respostaAula.componenteCurricular!.idComponenteCurricular}
            }
        })
    }

    componenteSelecionadoChanged(resposta: RespostaAulaComponente) {
        resposta.cargaHorariaSemanal = doubleToCargaHorariaComponent(this.max || 0);
        this.salvar(false).subscribe();
    }

    /**
     * Essa função só é ativdada, caso o valor seja válido, senão ele nem chama essa função
     * @param resposta
     */
    changeCargaHorariaResposta(resposta: RespostaAulaComponente) {
        this.salvar(false)
            .subscribe();
    }

    abrirDetalhamentoAulas() {
        this.listarRespostaAulas();
        this.showModalDetalhamento = true;
    }

    private fecharModal() {
        this.showModalDetalhamento = false;
    }

    salvar(fecharModal = true) {
        return new Observable(observer => {
            const respostaAtividadeSalvar = {
                atividade: {idAtividade: this.idAtividade},
                pit: {idPIT: this.idPit},
                detalhamentoComponentesCurriculares: this.getDetalhamentos()
            };

            this.apiService.post('respostas', respostaAtividadeSalvar)
                .subscribe((response: any) => {
                    notify('Resposta salva com sucesso', 'success', 2000);
                    this.verificarArrayRespostas();
                    this.totalHoras = doubleToCargaHorariaComponent(response.cargaHorariaSemanal);
                    if (fecharModal) this.fecharModal();
                    this.obterTotalHoras();
                    observer.next();
                }, e => {
                    notify(e.error.message, 'error', 4000);
                    observer.error();
                });
        });
    }
}
