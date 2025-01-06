import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITechnicalCV, NewTechnicalCV } from '../technical-cv.model';

export type PartialUpdateTechnicalCV = Partial<ITechnicalCV> & Pick<ITechnicalCV, 'id'>;

export type EntityResponseType = HttpResponse<ITechnicalCV>;
export type EntityArrayResponseType = HttpResponse<ITechnicalCV[]>;

@Injectable({ providedIn: 'root' })
export class TechnicalCVService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/technical-cvs');

  create(technicalCV: NewTechnicalCV): Observable<EntityResponseType> {
    return this.http.post<ITechnicalCV>(this.resourceUrl, technicalCV, { observe: 'response' });
  }

  update(technicalCV: ITechnicalCV): Observable<EntityResponseType> {
    return this.http.put<ITechnicalCV>(`${this.resourceUrl}/${this.getTechnicalCVIdentifier(technicalCV)}`, technicalCV, {
      observe: 'response',
    });
  }

  partialUpdate(technicalCV: PartialUpdateTechnicalCV): Observable<EntityResponseType> {
    return this.http.patch<ITechnicalCV>(`${this.resourceUrl}/${this.getTechnicalCVIdentifier(technicalCV)}`, technicalCV, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITechnicalCV>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITechnicalCV[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTechnicalCVIdentifier(technicalCV: Pick<ITechnicalCV, 'id'>): number {
    return technicalCV.id;
  }

  compareTechnicalCV(o1: Pick<ITechnicalCV, 'id'> | null, o2: Pick<ITechnicalCV, 'id'> | null): boolean {
    return o1 && o2 ? this.getTechnicalCVIdentifier(o1) === this.getTechnicalCVIdentifier(o2) : o1 === o2;
  }

  addTechnicalCVToCollectionIfMissing<Type extends Pick<ITechnicalCV, 'id'>>(
    technicalCVCollection: Type[],
    ...technicalCVSToCheck: (Type | null | undefined)[]
  ): Type[] {
    const technicalCVS: Type[] = technicalCVSToCheck.filter(isPresent);
    if (technicalCVS.length > 0) {
      const technicalCVCollectionIdentifiers = technicalCVCollection.map(technicalCVItem => this.getTechnicalCVIdentifier(technicalCVItem));
      const technicalCVSToAdd = technicalCVS.filter(technicalCVItem => {
        const technicalCVIdentifier = this.getTechnicalCVIdentifier(technicalCVItem);
        if (technicalCVCollectionIdentifiers.includes(technicalCVIdentifier)) {
          return false;
        }
        technicalCVCollectionIdentifiers.push(technicalCVIdentifier);
        return true;
      });
      return [...technicalCVSToAdd, ...technicalCVCollection];
    }
    return technicalCVCollection;
  }
}
