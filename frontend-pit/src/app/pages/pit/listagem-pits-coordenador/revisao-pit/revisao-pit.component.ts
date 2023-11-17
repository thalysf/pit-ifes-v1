import {Component, OnInit} from '@angular/core';
import {ApiService} from "../../../../shared/services/api.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ActionToolbar} from "../../../../shared/components/toolbar/toolbar.component";
import {Observable} from "rxjs";
import notify from "devextreme/ui/notify";
import {confirm} from "devextreme/ui/dialog";

@Component({
    selector: 'app-revisao-pit',
    templateUrl: './revisao-pit.component.html',
    styleUrls: ['./revisao-pit.component.scss']
})
export class RevisaoPitComponent implements OnInit {
    tituloPit: string = '';
    id!: string;
    actions!: ActionToolbar[];
    pit: any = {};
    showModalAlteracoes = false;
    textoAlteracoes = '';

    constructor(
        private apiService: ApiService,
        private route: ActivatedRoute,
        private router: Router
    ) {
    }

    ngOnInit(): void {
        this.id = this.route.snapshot.paramMap.get('id')!;
        this.detalharPit();
    }

    aprovar() {
        return new Observable(observer => {
            confirm('Tem certeza que deseja aprovar o PIT? Depois de aprovado não será possível responder novamente o mesmo PIT', 'Confirmar aprovação')
                .then(confirmado => {
                    if (confirmado) {
                        this.apiService.post(`pit/${this.id}/aprovar`, {})
                            .subscribe(response => {
                                this.router.navigate([`/pits/emRevisao`]);
                                observer.next();
                            }, error => {
                                notify(error.error.message, 'error', 4000);
                                observer.error();
                            })
                    } else {
                        observer.next();
                    }
                })
        })
    }

    openModalPedirAlteracao() {
        return new Observable(observer => {
            this.showModalAlteracoes = true;
            observer.next();
        })
    }

    pedirAlteracao() {
        return new Observable(observer => {
            this.apiService.post(`pit/${this.id}/pedirAlteracoes`, {
                texto: this.textoAlteracoes
            }).subscribe(response => {
                observer.next();
                this.router.navigate([`/pits/emRevisao`]);
            }, error => {
                notify(error.error.message, 'error', 4000);
                observer.error();
            })
        })
    }

    baixarPit() {
        return new Observable(observer => {
            const idPit = this.pit.idPIT;
            const periodo = this.pit.periodo;
            const professor = this.pit.professor.nome;

            this.apiService.getFile(`relatorios/${idPit}`)
                .subscribe((response: any) => {
                    observer.next();
                    const a = document.createElement('a');
                    document.body.appendChild(a);
                    const blob: any = new Blob([response], {type: 'octet/stream'});
                    const url = window.URL.createObjectURL(blob);
                    a.href = url;
                    a.download = `${periodo}-${professor}.xls`;
                    a.click();
                    window.URL.revokeObjectURL(url);
                }, e => {
                    notify(e.error.message, 'error', 4000);
                    observer.error();
                })
        })
    }

    private detalharPit() {
        this.apiService.get(`pit/${this.id}`)
            .subscribe((response: any) => {
                this.pit = response;
                this.tituloPit = `${response.periodo} - ${response.professor.nome}`;

                this.actions = [
                    {
                        type: 'danger',
                        icon: 'close',
                        text: 'Pedir alteração',
                        onClick: this.openModalPedirAlteracao()
                    },
                    {
                        text: 'Baixar excell',
                        icon: 'download',
                        type: 'success',
                        onClick: this.baixarPit(),
                    },
                ]

                if(!response.aprovado){
                    this.actions.push(
                        {
                            type: 'success',
                            icon: 'arrowright',
                            text: 'Aprovar',
                            onClick: this.aprovar(),
                        }
                    );
                }
            })
    }
}
