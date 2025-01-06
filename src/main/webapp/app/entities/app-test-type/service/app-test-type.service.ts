import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAppTestType, NewAppTestType } from '../app-test-type.model';

export type PartialUpdateAppTestType = Partial<IAppTestType> & Pick<IAppTestType, 'id'>;

export type EntityResponseType = HttpResponse<IAppTestType>;
export type EntityArrayResponseType = HttpResponse<IAppTestType[]>;

@Injectable({ providedIn: 'root' })
export class AppTestTypeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/app-test-types');

  create(appTestType: NewAppTestType): Observable<EntityResponseType> {
    return this.http.post<IAppTestType>(this.resourceUrl, appTestType, { observe: 'response' });
  }

  update(appTestType: IAppTestType): Observable<EntityResponseType> {
    return this.http.put<IAppTestType>(`${this.resourceUrl}/${this.getAppTestTypeIdentifier(appTestType)}`, appTestType, {
      observe: 'response',
    });
  }

  partialUpdate(appTestType: PartialUpdateAppTestType): Observable<EntityResponseType> {
    return this.http.patch<IAppTestType>(`${this.resourceUrl}/${this.getAppTestTypeIdentifier(appTestType)}`, appTestType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAppTestType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAppTestType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAppTestTypeIdentifier(appTestType: Pick<IAppTestType, 'id'>): number {
    return appTestType.id;
  }

  compareAppTestType(o1: Pick<IAppTestType, 'id'> | null, o2: Pick<IAppTestType, 'id'> | null): boolean {
    return o1 && o2 ? this.getAppTestTypeIdentifier(o1) === this.getAppTestTypeIdentifier(o2) : o1 === o2;
  }

  addAppTestTypeToCollectionIfMissing<Type extends Pick<IAppTestType, 'id'>>(
    appTestTypeCollection: Type[],
    ...appTestTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const appTestTypes: Type[] = appTestTypesToCheck.filter(isPresent);
    if (appTestTypes.length > 0) {
      const appTestTypeCollectionIdentifiers = appTestTypeCollection.map(appTestTypeItem => this.getAppTestTypeIdentifier(appTestTypeItem));
      const appTestTypesToAdd = appTestTypes.filter(appTestTypeItem => {
        const appTestTypeIdentifier = this.getAppTestTypeIdentifier(appTestTypeItem);
        if (appTestTypeCollectionIdentifiers.includes(appTestTypeIdentifier)) {
          return false;
        }
        appTestTypeCollectionIdentifiers.push(appTestTypeIdentifier);
        return true;
      });
      return [...appTestTypesToAdd, ...appTestTypeCollection];
    }
    return appTestTypeCollection;
  }
}
