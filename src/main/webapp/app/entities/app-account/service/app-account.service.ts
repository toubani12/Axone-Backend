import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAppAccount, NewAppAccount } from '../app-account.model';

export type PartialUpdateAppAccount = Partial<IAppAccount> & Pick<IAppAccount, 'id'>;

type RestOf<T extends IAppAccount | NewAppAccount> = Omit<T, 'endDate'> & {
  endDate?: string | null;
};

export type RestAppAccount = RestOf<IAppAccount>;

export type NewRestAppAccount = RestOf<NewAppAccount>;

export type PartialUpdateRestAppAccount = RestOf<PartialUpdateAppAccount>;

export type EntityResponseType = HttpResponse<IAppAccount>;
export type EntityArrayResponseType = HttpResponse<IAppAccount[]>;

@Injectable({ providedIn: 'root' })
export class AppAccountService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/app-accounts');

  create(appAccount: NewAppAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appAccount);
    return this.http
      .post<RestAppAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(appAccount: IAppAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appAccount);
    return this.http
      .put<RestAppAccount>(`${this.resourceUrl}/${this.getAppAccountIdentifier(appAccount)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(appAccount: PartialUpdateAppAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appAccount);
    return this.http
      .patch<RestAppAccount>(`${this.resourceUrl}/${this.getAppAccountIdentifier(appAccount)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAppAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAppAccount[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAppAccountIdentifier(appAccount: Pick<IAppAccount, 'id'>): number {
    return appAccount.id;
  }

  compareAppAccount(o1: Pick<IAppAccount, 'id'> | null, o2: Pick<IAppAccount, 'id'> | null): boolean {
    return o1 && o2 ? this.getAppAccountIdentifier(o1) === this.getAppAccountIdentifier(o2) : o1 === o2;
  }

  addAppAccountToCollectionIfMissing<Type extends Pick<IAppAccount, 'id'>>(
    appAccountCollection: Type[],
    ...appAccountsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const appAccounts: Type[] = appAccountsToCheck.filter(isPresent);
    if (appAccounts.length > 0) {
      const appAccountCollectionIdentifiers = appAccountCollection.map(appAccountItem => this.getAppAccountIdentifier(appAccountItem));
      const appAccountsToAdd = appAccounts.filter(appAccountItem => {
        const appAccountIdentifier = this.getAppAccountIdentifier(appAccountItem);
        if (appAccountCollectionIdentifiers.includes(appAccountIdentifier)) {
          return false;
        }
        appAccountCollectionIdentifiers.push(appAccountIdentifier);
        return true;
      });
      return [...appAccountsToAdd, ...appAccountCollection];
    }
    return appAccountCollection;
  }

  protected convertDateFromClient<T extends IAppAccount | NewAppAccount | PartialUpdateAppAccount>(appAccount: T): RestOf<T> {
    return {
      ...appAccount,
      endDate: appAccount.endDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restAppAccount: RestAppAccount): IAppAccount {
    return {
      ...restAppAccount,
      endDate: restAppAccount.endDate ? dayjs(restAppAccount.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAppAccount>): HttpResponse<IAppAccount> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAppAccount[]>): HttpResponse<IAppAccount[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
