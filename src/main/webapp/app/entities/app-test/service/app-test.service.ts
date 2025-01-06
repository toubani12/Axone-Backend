import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAppTest, NewAppTest } from '../app-test.model';

export type PartialUpdateAppTest = Partial<IAppTest> & Pick<IAppTest, 'id'>;

export type EntityResponseType = HttpResponse<IAppTest>;
export type EntityArrayResponseType = HttpResponse<IAppTest[]>;

@Injectable({ providedIn: 'root' })
export class AppTestService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/app-tests');

  create(appTest: NewAppTest): Observable<EntityResponseType> {
    return this.http.post<IAppTest>(this.resourceUrl, appTest, { observe: 'response' });
  }

  update(appTest: IAppTest): Observable<EntityResponseType> {
    return this.http.put<IAppTest>(`${this.resourceUrl}/${this.getAppTestIdentifier(appTest)}`, appTest, { observe: 'response' });
  }

  partialUpdate(appTest: PartialUpdateAppTest): Observable<EntityResponseType> {
    return this.http.patch<IAppTest>(`${this.resourceUrl}/${this.getAppTestIdentifier(appTest)}`, appTest, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAppTest>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAppTest[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAppTestIdentifier(appTest: Pick<IAppTest, 'id'>): number {
    return appTest.id;
  }

  compareAppTest(o1: Pick<IAppTest, 'id'> | null, o2: Pick<IAppTest, 'id'> | null): boolean {
    return o1 && o2 ? this.getAppTestIdentifier(o1) === this.getAppTestIdentifier(o2) : o1 === o2;
  }

  addAppTestToCollectionIfMissing<Type extends Pick<IAppTest, 'id'>>(
    appTestCollection: Type[],
    ...appTestsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const appTests: Type[] = appTestsToCheck.filter(isPresent);
    if (appTests.length > 0) {
      const appTestCollectionIdentifiers = appTestCollection.map(appTestItem => this.getAppTestIdentifier(appTestItem));
      const appTestsToAdd = appTests.filter(appTestItem => {
        const appTestIdentifier = this.getAppTestIdentifier(appTestItem);
        if (appTestCollectionIdentifiers.includes(appTestIdentifier)) {
          return false;
        }
        appTestCollectionIdentifiers.push(appTestIdentifier);
        return true;
      });
      return [...appTestsToAdd, ...appTestCollection];
    }
    return appTestCollection;
  }
}
