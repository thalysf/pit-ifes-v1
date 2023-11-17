import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {ApiService} from "../../../shared/services/api.service";
import {Observable} from "rxjs";
import {Aluno} from "../../../shared/model/aluno";
import {ConfigurationColumn} from "../../../shared/model/resource";
import {
    dataToDouble, doubleToCargaHorariaComponent,
    doubleToDate,
    incrementHora,
    stringHourFormatToDouble
} from "../../../shared/comum/cargaHoraria.resource";
import {TabelaListagemComponent} from "../../../shared/components/table-list/tabela-listagem/tabela-listagem.component";
import {
    FormularioDinamicoComponent
} from "../../../shared/components/formulario-dinamico/formulario-dinamico.component";
import notify from "devextreme/ui/notify";
import {confirm} from "devextreme/ui/dialog";
import {RespostaAtividade} from "../../../shared/model/respostaAtividade";
import {HoraConfiguration} from "../../../shared/components/carga-horaria-editor/carga-horaria-editor.component";

interface RespostaAluno {
    aluno?: Aluno,
    cargaHorariaSemanal: number,
    idDetalhamentoAluno?: string,
    tipoAcao: string,
    tipoAtuacao: string,
}

interface RespostaAlunoComponente {
    aluno?: Aluno,
    cargaHorariaSemanal: HoraConfiguration,
    idDetalhamentoAluno?: string,
    tipoAcao: string,
    tipoAtuacao: string,
}

@Component({
    selector: 'app-detalhamento-alunos',
    templateUrl: './detalhamento-alunos.component.html',
    styleUrls: ['./detalhamento-alunos.component.scss']
})
export class DetalhamentoAlunosComponent implements OnInit {
    @ViewChild('tabelaListagemAlunos', {static: false}) tabelaListagemAlunos!: TabelaListagemComponent;
    @ViewChild('formularioAluno', {static: false}) formularioAluno!: FormularioDinamicoComponent;

    @Input() nomeAtividade!: string;
    @Input() idAtividade!: string;
    @Input() idPit!: string;
    @Input() totalHoras!: HoraConfiguration;
    @Input() cargaHorariaMaxima!: number;
    @Input() cargaHorariaMinima!: number;
    @Input() obterTotalHoras!: Function;
    @Input() pitAprovadoOuEmRevisao!: boolean;

    showModalDetalhamento = false;
    showFormularioAluno = false;
    showModalListagemAlunos = false;
    entidadeAluno = {};
    colunasAlunos: ConfigurationColumn[];
    colunasDetalhamentoAlunos: ConfigurationColumn[];
    rotaAlunos: string = '';
    respostas: RespostaAlunoComponente[] = [];
    alunoSelecionadoModalListagem: any;

    constructor(private apiService: ApiService) {
        this.colunasAlunos = [
            {
                field: 'nome',
                required: true
            },
            {
                field: 'matricula',
                required: true
            }
        ]

        this.colunasDetalhamentoAlunos = [
            {
                field: 'nome',
                required: true
            },
            {
                field: 'matricula',
                required: true
            },
            {
                field: 'tipoAcao',
                required: true,
            },
            {
                field: 'tipoAtuacao',
                required: true
            }
        ]
    }

    ngOnInit(): void {
        this.rotaAlunos = `alunos`;
    }

    abrirDetalhamentoAlunos() {
        this.showModalDetalhamento = true;
        this.listarDetalhamentosAlunos();
    }

    fecharDetalhamentoAlunos(){
        this.showModalDetalhamento = false;
    }

    salvar() {
        return new Observable(observer => {
            this.fecharDetalhamentoAlunos();
            observer.next();
        })
    }

    changeCargaHorariaResposta(resposta: RespostaAlunoComponente) {
        this.salvarRespostaAluno(resposta).subscribe((response: any) => {
            resposta = response;
            this.verificarArrayRespostas();
            let total = 0;
            this.respostas.forEach(resposta => total = incrementHora(total, stringHourFormatToDouble(resposta.cargaHorariaSemanal.id)));
            this.totalHoras = doubleToCargaHorariaComponent(total);
            notify('Resposta atualizada com sucesso', 'success', 2000);
        })
    }

    onClickRemoverLinha(resposta: RespostaAlunoComponente) {
        confirm('Tem certeza que deseja remover o aluno selecionado?', 'Confirmar Exclusão')
            .then(confirmado => {
                if (confirmado) {
                    this.removerRespostaAluno(resposta);
                }
            })
    }

    verificarArrayRespostas() {
        if (this.respostas.length == 0 || this.respostas[this.respostas.length - 1]?.aluno) {
            this.criarRespostaVazia();
        }
    }

    private criarRespostaVazia() {
        this.respostas.push({
            cargaHorariaSemanal: doubleToCargaHorariaComponent(0),
            tipoAtuacao: '',
            tipoAcao: ''
        });
    }

    private listarDetalhamentosAlunos() {
        this.respostas = [];

        this.apiService.get('respostas/apoioEnsino/detalhamentoAlunos', {
            idAtividade: this.idAtividade,
            idPit: this.idPit
        })
            .subscribe(response => {
                const detalhamentos = response as any[];
                let total = 0;

                detalhamentos.forEach(detalhamento => {
                    total = incrementHora(total, detalhamento.cargaHorariaSemanal);

                    this.respostas.push({
                        cargaHorariaSemanal: doubleToCargaHorariaComponent(detalhamento.cargaHorariaSemanal),
                        aluno: detalhamento.aluno,
                        idDetalhamentoAluno: detalhamento.idDetalhamentoAluno,
                        tipoAtuacao: detalhamento.tipoAtuacao,
                        tipoAcao: detalhamento.tipoAcao
                    })
                })

                this.totalHoras = doubleToCargaHorariaComponent(total);

                this.verificarArrayRespostas();
            }, () => {
                this.verificarArrayRespostas();
            })
    }

    abrirFormularioInserirAluno(resposta: RespostaAlunoComponente) {
        this.showFormularioAluno = true;
    }

    fecharFormularioInserirAluno(){
        this.showFormularioAluno = false;
    }

    salvarFormularioAluno() {
        return new Observable(observer => {
            if(this.formularioAluno.isValid()){
                const dadosFormulario = this.formularioAluno.getDadosFormularioSalvar();
                const respostaAluno: RespostaAlunoComponente = {
                    aluno: {nome: dadosFormulario.nome, matricula: dadosFormulario.matricula},
                    cargaHorariaSemanal: doubleToCargaHorariaComponent(this.cargaHorariaMaxima),
                    tipoAcao: dadosFormulario.tipoAcao,
                    tipoAtuacao: dadosFormulario.tipoAtuacao,
                };

                this.salvarRespostaAluno(respostaAluno)
                    .subscribe(response => {
                        this.entidadeAluno = {};
                        this.listarDetalhamentosAlunos();
                        this.fecharFormularioInserirAluno();
                    })

                observer.next();
            } else {
                observer.error('Formulario inválido');
            }
        })
    }

    salvarRespostaAluno(resposta: RespostaAlunoComponente){
        const entity = {
            ...resposta,
            cargaHorariaSemanal: stringHourFormatToDouble(resposta.cargaHorariaSemanal.id)
        }
        return new Observable(observer => {
            this.apiService.post('respostas/apoioEnsino/detalhamentoAluno', entity, {
                idPit: this.idPit,
                idAtividade: this.idAtividade
            }).subscribe((response: any) => {
                this.obterTotalHoras();
                observer.next(response);
            }, e => {
                notify(e.error.message, 'error', 4000);
                observer.error(e);
            })
        })
    }

    removerRespostaAluno(resposta: RespostaAlunoComponente){
        this.apiService.delete('respostas/apoioEnsino/detalhamentoAluno', resposta.idDetalhamentoAluno!)
            .subscribe((response: any) => {
                this.respostas.splice(this.respostas.indexOf(resposta), 1);
                this.totalHoras = doubleToCargaHorariaComponent(response.cargaHorariaSemanal);
                notify('Resposta aluno removida com sucesso', 'success', 2000);
                this.obterTotalHoras();
            }, e => {
                notify(e.error.message, 'error', 4000);
            })
    }

    preencherDadosDetalhamentoAluno(detalhamento: RespostaAlunoComponente){
        // const resposta = this.respostas[this.respostas.length -1];
        // resposta.aluno = detalhamento.aluno;
        // resposta.cargaHorariaSemanal = this.cargaHorariaMaxima;
        // resposta.tipoAtuacao = detalhamento.tipoAtuacao;
        // resposta.tipoAcao = detalhamento.tipoAcao;
    }

    onSelecionarAlunoModalListagem() {
        return new Observable(observer => {
            const aluno = this.tabelaListagemAlunos.getSelectedEntities()[0];
            if(aluno){
                this.entidadeAluno = {
                    nome: aluno.nome,
                    matricula: aluno.matricula
                }
                this.fecharListagemAlunos();
                observer.next();
            } else {
                notify('Primeiro selecione um aluno', 'warning', 4000);
                observer.error();
            }
        })
    }

    abrirListagemAlunos() {
        this.showModalListagemAlunos = true;
        this.tabelaListagemAlunos.listarEntidades();
    }

    fecharListagemAlunos(){
        this.showModalListagemAlunos = false;
    }

    getDadosAluno(resposta: RespostaAlunoComponente) {
        if(!resposta.aluno) return '';
        else return `${resposta.aluno.nome} - ${resposta.aluno.matricula} - ${resposta.tipoAcao} -  ${resposta.tipoAtuacao}`;
    }
}
