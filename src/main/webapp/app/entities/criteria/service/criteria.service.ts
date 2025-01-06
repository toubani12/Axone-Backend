import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICriteria, NewCriteria } from '../criteria.model';

export type PartialUpdateCriteria = Partial<ICriteria> & Pick<ICriteria, 'id'>;

export type EntityResponseType = HttpResponse<ICriteria>;
export type EntityArrayResponseType = HttpResponse<ICriteria[]>;

@Injectable({ providedIn: 'root' })
export class CriteriaService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/criteria');

  create(criteria: NewCriteria): Observable<EntityResponseType> {
    return this.http.post<ICriteria>(this.resourceUrl, criteria, { observe: 'response' });
  }

  update(criteria: ICriteria): Observable<EntityResponseType> {
    return this.http.put<ICriteria>(`${this.resourceUrl}/${this.getCriteriaIdentifier(criteria)}`, criteria, { observe: 'response' });
  }

  partialUpdate(criteria: PartialUpdateCriteria): Observable<EntityResponseType> {
    return this.http.patch<ICriteria>(`${this.resourceUrl}/${this.getCriteriaIdentifier(criteria)}`, criteria, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICriteria>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICriteria[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCriteriaIdentifier(criteria: Pick<ICriteria, 'id'>): number {
    return criteria.id;
  }

  compareCriteria(o1: Pick<ICriteria, 'id'> | null, o2: Pick<ICriteria, 'id'> | null): boolean {
    return o1 && o2 ? this.getCriteriaIdentifier(o1) === this.getCriteriaIdentifier(o2) : o1 === o2;
  }

  addCriteriaToCollectionIfMissing<Type extends Pick<ICriteria, 'id'>>(
    criteriaCollection: Type[],
    ...criteriaToCheck: (Type | null | undefined)[]
  ): Type[] {
    const criteria: Type[] = criteriaToCheck.filter(isPresent);
    if (criteria.length > 0) {
      const criteriaCollectionIdentifiers = criteriaCollection.map(criteriaItem => this.getCriteriaIdentifier(criteriaItem));
      const criteriaToAdd = criteria.filter(criteriaItem => {
        const criteriaIdentifier = this.getCriteriaIdentifier(criteriaItem);
        if (criteriaCollectionIdentifiers.includes(criteriaIdentifier)) {
          return false;
        }
        criteriaCollectionIdentifiers.push(criteriaIdentifier);
        return true;
      });
      return [...criteriaToAdd, ...criteriaCollection];
    }
    return criteriaCollection;
  }
}
