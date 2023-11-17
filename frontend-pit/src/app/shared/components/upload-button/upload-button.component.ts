import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ApiService} from "../../services/api.service";
import notify from "devextreme/ui/notify";

@Component({
  selector: 'app-upload-button',
  templateUrl: './upload-button.component.html',
  styleUrls: ['./upload-button.component.scss']
})
export class UploadButtonComponent {

    @Input() router!: string;
    @Input() errorMessage: string = '';
    @Output() errorMessageChange = new EventEmitter();

    showLoadIndicator = false;

    constructor(private apiService: ApiService) {
    }

    uploadFile = (file: any) => {
        const formData = new FormData();
        formData.append('file', file);
        this.showLoadIndicator = true;

        this.apiService.postFile(this.router, formData)
            .subscribe(response => {
                this.showLoadIndicator = false;
                this.errorMessage = '';
                this.errorMessageChange.emit(this.errorMessage);
                notify('Resposta salva com sucesso', 'success', 2000);
            }, e => {
                // converter tipo de resposta 'texto' para json
                const error = JSON.parse(e.error);
                notify('Erro no arquivo', 'error', 2000);
                this.showLoadIndicator = false;
                this.errorMessage = error.message;
                this.errorMessageChange.emit(this.errorMessage);
            })
    };
}
