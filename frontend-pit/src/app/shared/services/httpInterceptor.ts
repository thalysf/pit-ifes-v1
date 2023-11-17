import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest,} from '@angular/common/http';
import {catchError, Observable} from 'rxjs';
import {AuthService} from "./auth.service";
import {Injectable} from "@angular/core";

@Injectable()
export class AddHeaderInterceptor implements HttpInterceptor {

    constructor(private authService: AuthService) {
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // Clone the request to add the new header
        const jwtToken = localStorage.getItem('jwtToken');
        const tokenType = localStorage.getItem('tokenType');

        let clonedRequest = req;

        if (jwtToken && tokenType) {
            clonedRequest = req.clone({headers: req.headers.append('Authorization', tokenType + ' ' + jwtToken)});
        }

        return next.handle(clonedRequest).pipe(catchError(error => this.handleError(error)));
    }

    private handleError(err: HttpErrorResponse): Observable<any> {
        return new Observable<any>(observer => {
            if (err.status === 401) {
                this.authService.logOut();
            }
            // handle your auth error or rethrow
            observer.error(err);
        })
    }
}
