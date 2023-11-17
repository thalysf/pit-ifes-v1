import {Component, Input} from '@angular/core';
import {ApiService} from "../../../shared/services/api.service";
import {ParticipacaoProjeto, Projeto} from "../../../shared/model/projeto";
import {
    dataToDouble,
    doubleToCargaHorariaComponent,
    doubleToDate,
    stringHourFormatToDouble
} from "../../../shared/comum/cargaHoraria.resource";
import {forkJoin, Observable} from "rxjs";
import notify from "devextreme/ui/notify";
import {AuthService} from "../../../shared/services";
import {ConfigurationColumn} from "../../../shared/model/resource";
import {confirm} from "devextreme/ui/dialog";
import {HoraConfiguration} from "../../../shared/components/carga-horaria-editor/carga-horaria-editor.component";

interface DetalhamentoProjetoComponente {
    cargaHorariaSemanal: HoraConfiguration,
    idDetalhamentoProjeto?: string,
    tipoParticipacao?: string,
    projeto?: Projeto
}

interface DetalhamentoProjeto {
    cargaHorariaSemanal: number,
    idDetalhamentoProjeto?: string,
    tipoParticipacao?: string,
    projeto?: Projeto
}

@Component({
    selector: 'app-detalhamento-projetos',
    templateUrl: './detalhamento-projetos.component.html',
    styleUrls: [
        './detalhamento-projetos.component.scss',
    ]
})
export class DetalhamentoProjetosComponent {

    @Input() nomeAtividade!: string;
    @Input() idAtividade!: string;
    @Input() idPit!: string;
    @Input() totalHoras!: HoraConfiguration;
    @Input() obterTotalHoras!: Function;
    @Input() pitAprovadoOuEmRevisao!: boolean;

    showLoadIndicator = false;
    showModalDetalhamentoProjeto = false;
    showModalIncluirParticipacaoProjeto = false;
    colunasProjetos: ConfigurationColumn[] = [];
    respostas: DetalhamentoProjetoComponente[] = [];
    zeroEmHoras = 0;

    // formulario incluir participação projeto
    projetoSelecionado: ParticipacaoProjeto | null = null;
    tipoParticipacao: string = '';

    constructor(private apiService: ApiService, private authService: AuthService) {
        this.colunasProjetos = [
            {
                field: 'projeto.tituloProjeto',
                label: 'Projeto',
            },
            {
                field: 'projeto.numeroCadastro',
                label: 'Cadastro',
            },
        ]
    }

    abrirDetalhamentoAtividadeProjeto() {
        this.showModalDetalhamentoProjeto = true;
        this.listarProjetosAtividade();
    }

    getUrlProjetosAssociadosAoProfessorEAtividade(){
        return `atividades/${this.idAtividade}/projetos/${this.authService.getUserId()}`;
    }

    private listarProjetosAtividade() {
        this.respostas = [];
        this.showLoadIndicator = true;
        const observerDetalhamento = this.apiService.get(`respostas/${this.idAtividade}/pit/${this.idPit}/detalhamento_projetos`)

        forkJoin([observerDetalhamento])
            .subscribe(([respostaDetalhamentos]: any) => {
                const detalhamentos = respostaDetalhamentos as DetalhamentoProjeto[];
                detalhamentos.forEach(respostaProjeto => {

                    this.respostas.push({
                        cargaHorariaSemanal: doubleToCargaHorariaComponent(respostaProjeto.cargaHorariaSemanal),
                        idDetalhamentoProjeto: respostaProjeto.idDetalhamentoProjeto,
                        projeto: {
                            ...respostaProjeto.projeto!,
                            cargaHorariaMaxima: respostaProjeto.projeto!.cargaHorariaMaxima,
                            cargaHorariaMinima: respostaProjeto.projeto!.cargaHorariaMinima,
                        },
                        tipoParticipacao: respostaProjeto.tipoParticipacao
                    });
                })

                this.showLoadIndicator = false;
                this.verificarArrayRespostas();
            })
    }

    salvar(fecharModal = true) {
        return new Observable(observer => {

            const respostaAtividadeSalvar = {
                atividade: {idAtividade: this.idAtividade},
                pit: {idPIT: this.idPit},
                detalhamentoProjetos: this.getDetalhamentos()
            };

            this.apiService.post('respostas', respostaAtividadeSalvar)
                .subscribe((response: any) => {
                    notify('Resposta salva com sucesso', 'success', 2000);
                    observer.next();
                    this.totalHoras = doubleToCargaHorariaComponent(response.cargaHorariaSemanal);
                    if(fecharModal) this.fecharModal();
                    this.obterTotalHoras();
                }, error => {
                    notify(error.error.message, 'error', 4000);
                    observer.error();
                })
        })
    }

    private fecharModal() {
        this.showModalDetalhamentoProjeto = false;
    }

    private getDetalhamentos() {
        return this.respostas
            .filter(resposta => resposta.projeto != null)
            .map(resposta => {
            return {
                cargaHorariaSemanal: stringHourFormatToDouble(resposta.cargaHorariaSemanal.id),
                projeto: {idProjeto: resposta.projeto!.idProjeto},
                tipoParticipacao: resposta.tipoParticipacao
            }
        })
    }

    changeCargaHorariaResposta() {
        this.salvar(false)
            .subscribe();
    }

    abrirModalIncluirParticipacaoProjeto() {
        this.showModalIncluirParticipacaoProjeto = true;
    }

    incluirParticipacaoProjeto() {
        return new Observable(observer => {
            if(this.tipoParticipacao == '' || this.tipoParticipacao == null){
                observer.error('Informe um tipo de participação correto');
                notify('Informe um tipo de participação!', 'error', 4000);
            } else if(this.projetoSelecionado == null) {
                observer.error('Informe um projeto');
                notify('Selecione um projeto!', 'error', 4000);
            } else {
                this.respostas.push({
                    cargaHorariaSemanal: doubleToCargaHorariaComponent(this.projetoSelecionado.projeto.cargaHorariaMaxima),
                    projeto: this.projetoSelecionado.projeto,
                    tipoParticipacao: this.tipoParticipacao
                })
                this.salvar(false).subscribe(response => {
                    this.showModalIncluirParticipacaoProjeto = false;
                    this.verificarArrayRespostas();
                    this.listarProjetosAtividade();
                    this.tipoParticipacao = '';
                    this.projetoSelecionado = null;
                    observer.next();
                }, () => {
                    observer.error();
                });
            }
        })
    }

    onClickRemoverLinha(resposta: DetalhamentoProjetoComponente) {
        confirm('Tem certeza que deseja remover a participação informada?', 'Confirmar Exclusão')
            .then(confirmado => {
                if (confirmado) {
                    this.excluirRespostaProjeto(resposta);
                }
            })
    }

    excluirRespostaProjeto(resposta: DetalhamentoProjetoComponente){
        this.apiService.delete('respostas/detalhamento_projetos', resposta.idDetalhamentoProjeto!)
            .subscribe(response => {
                this.listarProjetosAtividade();
            }, error => {
                notify(error.error.message, 'error', 2000);
            })
    }

    verificarArrayRespostas() {
        const respostaVazia = this.respostas.find(resposta => resposta.projeto == null);
        if(respostaVazia) this.removerRespostaArray(respostaVazia);
        this.criarRespostaVazia();
    }

    private removerRespostaArray(resposta: DetalhamentoProjetoComponente){
        this.respostas.splice(this.respostas.indexOf(resposta), 1);
    }

    private criarRespostaVazia() {
        this.respostas.push({
            cargaHorariaSemanal: doubleToCargaHorariaComponent(0),
        })
    }
}
