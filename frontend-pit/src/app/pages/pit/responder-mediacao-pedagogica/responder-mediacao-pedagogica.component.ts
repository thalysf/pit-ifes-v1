import {Component, Input, OnInit} from '@angular/core';
import {ConfigurationColumn} from "../../../shared/model/resource";
import {ApiService} from "../../../shared/services/api.service";
import {AuthService} from "../../../shared/services";
import {confirm} from "devextreme/ui/dialog";
import {ComponenteCurricular} from "../../../shared/model/componenteCurricular";
import {doubleToCargaHorariaComponent, stringHourFormatToDouble} from "../../../shared/comum/cargaHoraria.resource";
import notify from "devextreme/ui/notify";
import {HoraConfiguration} from "../../../shared/components/carga-horaria-editor/carga-horaria-editor.component";

interface DetalhamentoComponenteCurricularComponente {
    componenteCurricular?: ComponenteCurricular,
    cargaHorariaSemanal: HoraConfiguration,
    cargaHorariaMaxima: number,
    idDetalhamentoComponenteCurricular?: string,
}

@Component({
    selector: 'app-responder-mediacao-pedagogica',
    templateUrl: './responder-mediacao-pedagogica.component.html',
    styleUrls: ['./responder-mediacao-pedagogica.component.scss']
})
export class ResponderMediacaoPedagogicaComponent implements OnInit {

    @Input() idPit!: string;
    @Input() obterTotalHoras!: Function;
    @Input() pitAprovadoOuEmRevisao!: boolean;

    colunasComponentesCurriculares: ConfigurationColumn[];
    rotaComponentes = '';
    showLoadIndicator = false;

    respostas: DetalhamentoComponenteCurricularComponente[] = [];

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
        this.rotaComponentes = `mediacaoPedagogica/componentes_nao_detalhados/${this.idPit}`;
        this.listarRespostaAulas();
    }

    ngOnDestroy(): void {

    }

    onClickRemoverLinha(resposta: DetalhamentoComponenteCurricularComponente) {
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

    removerLinha(resposta: DetalhamentoComponenteCurricularComponente) {
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

    private salvarResposta(resposta: DetalhamentoComponenteCurricularComponente) {
        const entitySave = {
            idUsuario: this.authService.getUserId(),
            idPit: this.idPit,
            cargaHorariaSemanal: stringHourFormatToDouble(resposta.cargaHorariaSemanal.id),
            componenteCurricular: {
                idComponenteCurricular: resposta.componenteCurricular?.idComponenteCurricular
            }
        };

        let requestObserver;

        if (resposta.idDetalhamentoComponenteCurricular) {
            requestObserver = this.apiService.put('mediacaoPedagogica/resposta', resposta.idDetalhamentoComponenteCurricular, entitySave);
        } else {
            requestObserver = this.apiService.post('mediacaoPedagogica/resposta', entitySave);
        }

        requestObserver.subscribe((response: any) => {
            resposta.idDetalhamentoComponenteCurricular = response.idDetalhamentoComponenteCurricular;
            notify('Resposta criada com sucesso!', 'success', 1000);
            this.obterTotalHoras();
            this.verificarArrayRespostas();
        }, e => {
            this.removerLinha(resposta);
            notify(e.error.message, 'error', 2000);
        });
    }

    private listarRespostaAulas() {
        this.showLoadIndicator = true;
        this.apiService.get('mediacaoPedagogica/respostas', {
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

    private deletarRespostaAula(resposta: DetalhamentoComponenteCurricularComponente) {

        this.apiService.delete('mediacaoPedagogica/detalhamentos', resposta.idDetalhamentoComponenteCurricular!)
            .subscribe(response => {
                notify('Resposta deletada com sucesso!', 'success', 1000);
                this.obterTotalHoras();
                this.removerLinha(resposta);
            }, e => {
                notify(e.error.message, 'error', 2000);
            })
    }

    componenteSelecionadoChanged(resposta: DetalhamentoComponenteCurricularComponente) {
        resposta.cargaHorariaSemanal = doubleToCargaHorariaComponent(resposta.componenteCurricular?.cargaHoraria || 0);
        resposta.cargaHorariaMaxima = resposta.componenteCurricular?.cargaHoraria || 0;
        this.salvarResposta(resposta);
    }

    /**
     * Essa função só é ativdada, caso o valor seja válido, senão ele nem chama essa função
     * @param resposta
     */
    changeCargaHorariaResposta(resposta: DetalhamentoComponenteCurricularComponente) {
        this.salvarResposta(resposta);
    }
}
