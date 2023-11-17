import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {ConfigurationColumn} from "../../../shared/model/resource";
import {confirm} from "devextreme/ui/dialog";
import notify from "devextreme/ui/notify";
import {ComponenteCurricular} from "../../../shared/model/componenteCurricular";
import {ApiService} from "../../../shared/services/api.service";
import {AuthService} from "../../../shared/services";
import {
    dataToDouble,
    doubleToCargaHorariaComponent,
    doubleToDate,
    stringHourFormatToDouble
} from "../../../shared/comum/cargaHoraria.resource";
import {AtividadeDefaultEnum, NomeAtividadeDefaultEnum} from "../../../shared/enum/atividadeDefaultEnum";
import {HoraConfiguration} from "../../../shared/components/carga-horaria-editor/carga-horaria-editor.component";

interface RespostaAula {
    componenteCurricular?: ComponenteCurricular,
    cargaHorariaSemanal: HoraConfiguration,
    cargaHorariaMaxima: number,
    idDetalhamentoComponenteCurricular?: string,
}

@Component({
    selector: 'responder-aulas',
    templateUrl: './responder-aulas.component.html',
    styleUrls: ['./responder-aulas.component.scss']
})
export class ResponderAulasComponent implements OnInit, OnDestroy {

    @Input() idPit!: string;
    @Input() pitAprovadoOuEmRevisao!: boolean;
    @Input() obterTotalHoras!: Function;

    idAtividade = AtividadeDefaultEnum.AULA;
    nomeAtividade = NomeAtividadeDefaultEnum.AULA;

    colunasComponentesCurriculares: ConfigurationColumn[];
    colunasFilter = ['nome_curso', 'nome'];
    rotaComponentes = '';
    showLoadIndicator = false;

    respostas: RespostaAula[] = [];

    constructor(private apiService: ApiService, private authService: AuthService) {
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
        this.rotaComponentes = `aulas/componentes_nao_detalhados/${this.idPit}`;
        this.listarRespostaAulas();
    }

    ngOnDestroy(): void {

    }

    onClickRemoverLinha(resposta: RespostaAula) {
        if (!resposta.idDetalhamentoComponenteCurricular) {
            this.removerLinha(resposta);
        } else {
            confirm('Tem certeza que deseja remover o Componente Curricular selecionado?', 'Confirmar Exclusão')
                .then(confirmado => {
                    if (confirmado) {
                        this.deletarRespostaAula(resposta);
                    }
                })
        }
    }

    removerLinha(resposta: RespostaAula) {
        this.respostas.splice(this.respostas.indexOf(resposta), 1);
        this.verificarArrayRespostas();
    }

    verificarArrayRespostas() {
        if (this.respostas.length == 0 || this.respostas[this.respostas.length - 1]?.componenteCurricular) {
            this.criarRespostaVazia();
        }
    }

    formatNomeComponente(componente: ComponenteCurricular) {
        return `${componente?.nome} (${componente?.curso?.nome})`;
    }

    private salvarResposta(resposta: RespostaAula) {
        const entitySave = {
            idUsuario: this.authService.getUserId(),
            idPit: this.idPit,
            cargaHorariaSemanal: stringHourFormatToDouble(resposta.cargaHorariaSemanal.id),
            componenteCurricular: {
                idComponenteCurricular: resposta.componenteCurricular?.idComponenteCurricular,
            }
        };

        let requestObserver;

        if (resposta.idDetalhamentoComponenteCurricular) {
            requestObserver = this.apiService.put('aulas/respostaAula', resposta.idDetalhamentoComponenteCurricular, entitySave);
        } else {
            requestObserver = this.apiService.post('aulas/respostaAula', entitySave);
        }

        requestObserver.subscribe((response: any) => {
            resposta.idDetalhamentoComponenteCurricular = response.idDetalhamentoComponenteCurricular;
            notify('Resposta criada com sucesso!', 'success', 1000);
            this.verificarArrayRespostas();
            this.obterTotalHoras();
        }, e => {
            notify(e.error.message, 'error', 2000);
            this.listarRespostaAulas();
        });
    }

    private listarRespostaAulas() {
        this.respostas = [];
        this.showLoadIndicator = true;
        this.apiService.get('aulas/respostaAulas', {
            idPit: this.idPit
        }).subscribe((response: any) => {
            this.showLoadIndicator = false;
            const detalhamentos = response as any[];

            detalhamentos.forEach(detalhamento => {
                this.respostas.push({
                    cargaHorariaSemanal: doubleToCargaHorariaComponent(detalhamento.cargaHorariaSemanal),
                    componenteCurricular: detalhamento.componenteCurricular,
                    idDetalhamentoComponenteCurricular: detalhamento.idDetalhamentoComponenteCurricular,
                    cargaHorariaMaxima: detalhamento.componenteCurricular.cargaHoraria
                })
            })

            this.criarRespostaVazia();
        }, () => {
            this.showLoadIndicator = false;
        })
    }

    private criarRespostaVazia() {
        this.respostas.push({
            cargaHorariaSemanal: doubleToCargaHorariaComponent(0),
            cargaHorariaMaxima: 0
        });
    }

    private deletarRespostaAula(resposta: RespostaAula) {

        this.apiService.delete('aulas/detalhamentos', resposta.idDetalhamentoComponenteCurricular!)
            .subscribe(response => {
                notify('Resposta deletada com sucesso!', 'success', 1000);
                this.removerLinha(resposta);
                this.obterTotalHoras();
            }, e => {
                notify(e.error.message, 'error', 2000);
            })
    }

    componenteSelecionadoChanged(resposta: RespostaAula) {
        resposta.cargaHorariaSemanal = doubleToCargaHorariaComponent(resposta.componenteCurricular?.cargaHoraria || 0);
        resposta.cargaHorariaMaxima = resposta.componenteCurricular?.cargaHoraria || 0;
        this.salvarResposta(resposta);
    }

    /**
     * Essa função só é ativdada, caso o valor seja válido, senão ele nem chama essa função
     * @param resposta
     */
    changeCargaHorariaResposta(resposta: RespostaAula) {
        this.salvarResposta(resposta);
    }
}
