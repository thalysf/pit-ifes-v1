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
import {confirm} from "devextreme/ui/dialog"

@Component({
    selector: 'professores-portaria',
    templateUrl: './professores-portaria.component.html',
    styleUrls: ['./professores-portaria.component.scss']
})
export class ProfessoresPortariaComponent {

    @ViewChild('tabelaProfessoresPortaria', {static: false}) tabelaProfessoresPortaria!: TabelaListagemComponent;
    @ViewChild('tabelaProfessoresAssociar', {static: false}) tabelaProfessoresAssociar!: TabelaListagemComponent;

    @Input() idPortaria!: string;

    colunasProfessores: ConfigurationColumn[] = [];
    acoesTabelaProfessores: ActionsTabela[] = [];
    showModalAssociarProfessores = false;

    professorSelecionado: any = null;
    tipoParticipacaoSelecionada: any = null;

    constructor(private apiService: ApiService) {
        this.colunasProfessores = professorResource.colunas;

        this.acoesTabelaProfessores = [
            {
                type: 'success',
                icon: 'link',
                text: '',
                mode: 'batch',
                hint: 'Associar professores à portaria',
                onClick: this.abrirModalAssociarProfessores
            },
            {
                type: 'success',
                icon: 'trash',
                text: '',
                mode: 'single',
                hint: 'Remover professor da portaria',
                onClick: this.removerProfessorPortaria
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

    associarProfessorPortaria() {
        return new Observable<Object>(observer => {
            const idProfessorSelecionado = this.tabelaProfessoresAssociar.getSelectedIds()[0];
            if (!idProfessorSelecionado) {
                notify("Selecione um professor!", 'error', 2000);
                observer.error();
            } else {
                this.apiService.post(`portarias/${this.idPortaria}/professores/${idProfessorSelecionado}`, {}).subscribe(response => {
                    observer.next();
                    this.tabelaProfessoresPortaria.listarEntidades();
                    this.fecharModalAssociarProfessores();
                }, error => {
                    observer.next(error);
                })
            }
        })
    }

    removerProfessorPortaria = (data: any) => {
        confirm(
            'Tem certeza que deseja remover o professor dessa portaria? Isso não excluirá o professor, apenas o removerá da portaria.',
            'Confirmar exclusão'
        ).then(confirmado => {
            if (confirmado) {
                const professor = data.row.data;
                const rota = `portarias/${this.idPortaria}/professores/${professor.idServidor}`;
                this.apiService.delete(rota, '').subscribe(response => {
                    this.tabelaProfessoresPortaria.listarEntidades();
                }, error => {
                    notify(error.error.message, 'error', 4000);
                })
            }
        })
    }

    getUrlProfessoresPortaria() {
        return `portarias/${this.idPortaria}/professores`;
    }

    getUrlProfessoresNaoParticipantesPortaria() {
        return `portarias/${this.idPortaria}/professores_nao_participantes`;
    }

    onSelectedProfessorChanged($event: any) {
        this.professorSelecionado = $event;
    }

    onTipoParticipacaoChanged($event: any) {
        this.tipoParticipacaoSelecionada = $event;
    }
}
