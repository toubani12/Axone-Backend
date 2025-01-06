import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAppAccountType, NewAppAccountType } from '../app-account-type.model';

export type PartialUpdateAppAccountType = Partial<IAppAccountType> & Pick<IAppAccountType, 'id'>;

export type EntityResponseType = HttpResponse<IAppAccountType>;
export type EntityArrayResponseType = HttpResponse<IAppAccountType[]>;

@Injectable({ providedIn: 'root' })
export class AppAccountTypeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/app-account-types');

  create(appAccountType: NewAppAccountType): Observable<EntityResponseType> {
    return this.http.post<IAppAccountType>(this.resourceUrl, appAccountType, { observe: 'response' });
  }

  update(appAccountType: IAppAccountType): Observable<EntityResponseType> {
    return this.http.put<IAppAccountType>(`${this.resourceUrl}/${this.getAppAccountTypeIdentifier(appAccountType)}`, appAccountType, {
      observe: 'response',
    });
  }

  partialUpdate(appAccountType: PartialUpdateAppAccountType): Observable<EntityResponseType> {
    return this.http.patch<IAppAccountType>(`${this.resourceUrl}/${this.getAppAccountTypeIdentifier(appAccountType)}`, appAccountType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAppAccountType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAppAccountType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAppAccountTypeIdentifier(appAccountType: Pick<IAppAccountType, 'id'>): number {
    return appAccountType.id;
  }

  compareAppAccountType(o1: Pick<IAppAccountType, 'id'> | null, o2: Pick<IAppAccountType, 'id'> | null): boolean {
    return o1 && o2 ? this.getAppAccountTypeIdentifier(o1) === this.getAppAccountTypeIdentifier(o2) : o1 === o2;
  }

  addAppAccountTypeToCollectionIfMissing<Type extends Pick<IAppAccountType, 'id'>>(
    appAccountTypeCollection: Type[],
    ...appAccountTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const appAccountTypes: Type[] = appAccountTypesToCheck.filter(isPresent);
    if (appAccountTypes.length > 0) {
      const appAccountTypeCollectionIdentifiers = appAccountTypeCollection.map(appAccountTypeItem =>
        this.getAppAccountTypeIdentifier(appAccountTypeItem),
      );
      const appAccountTypesToAdd = appAccountTypes.filter(appAccountTypeItem => {
        const appAccountTypeIdentifier = this.getAppAccountTypeIdentifier(appAccountTypeItem);
        if (appAccountTypeCollectionIdentifiers.includes(appAccountTypeIdentifier)) {
          return false;
        }
        appAccountTypeCollectionIdentifiers.push(appAccountTypeIdentifier);
        return true;
      });
      return [...appAccountTypesToAdd, ...appAccountTypeCollection];
    }
    return appAccountTypeCollection;
  }
}
