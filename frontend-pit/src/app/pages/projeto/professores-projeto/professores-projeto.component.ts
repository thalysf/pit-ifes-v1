import {Component, Input, ViewChild} from '@angular/core';
import {ConfigurationColumn, SelectConfiguration} from "../../../shared/model/resource";
import {
    ActionsTabela,
    TabelaListagemComponent
} from "../../../shared/components/table-list/tabela-listagem/tabela-listagem.component";
import {professorResource} from "../../professores/professores.resource";
import {Observable} from "rxjs";
import notify from "devextreme/ui/notify";
import {ApiService} from "../../../shared/services/api.service";
import {participacaoProjetoResource} from "../../professores/participacao-projeto.resource";
import {confirm} from "devextreme/ui/dialog"

@Component({
  selector: 'professores-projeto',
  templateUrl: './professores-projeto.component.html',
  styleUrls: ['./professores-projeto.component.scss']
})
export class ProfessoresProjetoComponent {

    @ViewChild('tabelaProfessoresProjeto', {static: false}) tabelaProfessoresProjeto!: TabelaListagemComponent;
    @ViewChild('tabelaProfessoresAssociar', {static: false}) tabelaProfessoresAssociar!: TabelaListagemComponent;

    @Input() idProjeto!: string;

    colunasParticipantes: ConfigurationColumn[] = [];
    colunasProfessores: ConfigurationColumn[] = [];
    acoesTabelaProfessores: ActionsTabela[] = [];
    showModalAssociarProfessores = false;

    professorSelecionado: any = null;
    tipoParticipacaoSelecionada: any = null;

    configurationTipoParticipacao: SelectConfiguration = {
        route: 'projetos/tipo_participacao_projeto',
        displayExpr: 'nome',
        valueExpr: 'id',
    }


    constructor(private apiService: ApiService) {
        this.colunasParticipantes = participacaoProjetoResource.colunas;
        this.colunasProfessores = professorResource.colunas;

        this.acoesTabelaProfessores = [
            {
                type: 'success',
                icon: 'link',
                text: '',
                mode: 'batch',
                hint: 'Associar professores ao projeto',
                onClick: this.abrirModalAssociarProfessores
            },
            {
                type: 'success',
                icon: 'trash',
                text: '',
                mode: 'single',
                hint: 'Remover professor do projeto',
                onClick: this.removerProfessorProjeto
            }
        ];
    }

    abrirModalAssociarProfessores = () => {
        this.showModalAssociarProfessores = true;
        this.tabelaProfessoresAssociar.listarEntidades();
    }

    fecharModalAssociarProfessores() {
        this.showModalAssociarProfessores = false;
    }

    associarProfessorProjeto() {
        return new Observable<Object>(observer => {
            const idProfessorSelecionado = this.tabelaProfessoresAssociar.getSelectedIds()[0];
            if(!idProfessorSelecionado){
                notify("Selecione um professor!", 'error', 2000);
                observer.error();
            } else {
                this.apiService.post(`projetos/${this.idProjeto}/participacao_projeto`, {
                    idProfessor: idProfessorSelecionado,
                }).subscribe(response => {
                    observer.next();
                    this.tabelaProfessoresProjeto.listarEntidades();
                    this.fecharModalAssociarProfessores();
                }, error => {
                    observer.next(error);
                })
            }
        })
    }

    removerProfessorProjeto = (data: any) => {
        confirm(
            'Tem certeza que deseja remover o professor desse projeto? Isso não excluirá o professor, apenas o removerá do projeto.',
            'Confirmar exclusão'
        ).then(confirmado => {
            if(confirmado){
                const professorProjeto = data.row.data;
                const rota = `projetos/${this.idProjeto}/professores/${professorProjeto.professor.idServidor}/participacao_projeto`;
                this.apiService.delete(rota, '').subscribe(response => {
                    this.tabelaProfessoresProjeto.listarEntidades();
                }, error => {

                })
            }
        })
    }

    getUrlProfessoresProjeto() {
        return `projetos/${this.idProjeto}/professores_participantes`;
    }

    getUrlProfessoresNaoParticipantesProjeto(){
        return `projetos/${this.idProjeto}/professores_nao_participantes`;
    }

    onSelectedProfessorChanged($event: any) {
        this.professorSelecionado = $event;
    }

    onTipoParticipacaoChanged($event: any) {
        this.tipoParticipacaoSelecionada = $event;
    }
}
