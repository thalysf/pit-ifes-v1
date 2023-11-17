import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router} from '@angular/router';
import {ApiService} from "./api.service";
import {delay, map, Observable} from "rxjs";
import {RoleEnum} from "../enum/roleEnum";

export interface IUser {
    email: string;
    avatarUrl?: string
}

const defaultPath = '/';

@Injectable()
export class AuthService {
    private _user: IUser | null = null;

    get loggedIn(): boolean {
        return !!this._user;
    }

    private _lastAuthenticatedPath: string = defaultPath;
    set lastAuthenticatedPath(value: string) {
        this._lastAuthenticatedPath = value;
    }

    constructor(private router: Router, private apiService: ApiService) {
        const email = localStorage.getItem('email');
        if(email){
            this._user = {email: email};
        }
    }

    logIn(email: string, password: string) {
        return new Observable(observer => {
            this.apiService.post('login', {username: email, password: password}).pipe(delay(1000)).subscribe(
                (response: any) => {
                    if(!response['jwt']['accessToken']) observer.error('Erro ao realizar login');
                    this._user = {email: email};

                    observer.next(response);
                }, error => {
                    observer.error('Login ou senha inválidos');
                }
            );
        })
    }

    getUserId(){
        return localStorage.getItem('userId');
    }

    async getUser() {
        try {
            // Send request

            return {
                isOk: true,
                data: this._user
            };
        } catch {
            return {
                isOk: false,
                data: null
            };
        }
    }

    async createAccount() {
        try {
            // Send request
            this.router.navigate(['/create-account']);
            return {
                isOk: true
            };
        } catch {
            return {
                isOk: false,
                message: "Failed to create account"
            };
        }
    }

    public changePassword(email: string, newPassword: string, recoveryCode: string) {
        return new Observable(observer => {
            this.apiService.patch('user/password/reset', {
                email: email,
                recoveryCode: recoveryCode,
                newPassword: newPassword
            }).subscribe(response => {
                observer.next(response);
            }, error => {
                observer.error(error);
            });
        })
    }

    public sendRecoverEmail(email: string) {
        return new Observable(observer => {
            this.apiService.post('user/password/recover', email)
                .subscribe(response => {
                    observer.next(response);
                }, error => {
                    observer.error(error);
                })
        })
    }

    async logOut() {
        localStorage.clear();
        this._user = null;
        this.router.navigate(['/login']);
    }

    getRolesUser() {
        const roles: {roleId: string, roleName: RoleEnum, authority: string}[] = JSON.parse(localStorage.getItem('roles')!);
        return roles.map(role => role.roleName);
    }
}

@Injectable()
export class AuthGuardService implements CanActivate {
    constructor(private router: Router, private authService: AuthService) {
    }

    canActivate(route: ActivatedRouteSnapshot): boolean {
        const isLoggedIn = this.authService.loggedIn;
        const isAuthForm = [
            'login',
            'reset-password',
            'create-account',
            'change-password'
        ].includes(route.routeConfig?.path || defaultPath);

        if (isLoggedIn && isAuthForm) {
            this.authService.lastAuthenticatedPath = defaultPath;
            this.router.navigate([defaultPath]);
            return false;
        }

        if (!isLoggedIn && !isAuthForm) {
            this.router.navigate(['/login']);
        }

        if (isLoggedIn) {
            this.authService.lastAuthenticatedPath = route.routeConfig?.path || defaultPath;
        }

        return isLoggedIn || isAuthForm;
    }
}
