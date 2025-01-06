import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITechCVDocs, NewTechCVDocs } from '../tech-cv-docs.model';

export type PartialUpdateTechCVDocs = Partial<ITechCVDocs> & Pick<ITechCVDocs, 'id'>;

export type EntityResponseType = HttpResponse<ITechCVDocs>;
export type EntityArrayResponseType = HttpResponse<ITechCVDocs[]>;

@Injectable({ providedIn: 'root' })
export class TechCVDocsService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tech-cv-docs');

  create(techCVDocs: NewTechCVDocs): Observable<EntityResponseType> {
    return this.http.post<ITechCVDocs>(this.resourceUrl, techCVDocs, { observe: 'response' });
  }

  update(techCVDocs: ITechCVDocs): Observable<EntityResponseType> {
    return this.http.put<ITechCVDocs>(`${this.resourceUrl}/${this.getTechCVDocsIdentifier(techCVDocs)}`, techCVDocs, {
      observe: 'response',
    });
  }

  partialUpdate(techCVDocs: PartialUpdateTechCVDocs): Observable<EntityResponseType> {
    return this.http.patch<ITechCVDocs>(`${this.resourceUrl}/${this.getTechCVDocsIdentifier(techCVDocs)}`, techCVDocs, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITechCVDocs>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITechCVDocs[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTechCVDocsIdentifier(techCVDocs: Pick<ITechCVDocs, 'id'>): number {
    return techCVDocs.id;
  }

  compareTechCVDocs(o1: Pick<ITechCVDocs, 'id'> | null, o2: Pick<ITechCVDocs, 'id'> | null): boolean {
    return o1 && o2 ? this.getTechCVDocsIdentifier(o1) === this.getTechCVDocsIdentifier(o2) : o1 === o2;
  }

  addTechCVDocsToCollectionIfMissing<Type extends Pick<ITechCVDocs, 'id'>>(
    techCVDocsCollection: Type[],
    ...techCVDocsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const techCVDocs: Type[] = techCVDocsToCheck.filter(isPresent);
    if (techCVDocs.length > 0) {
      const techCVDocsCollectionIdentifiers = techCVDocsCollection.map(techCVDocsItem => this.getTechCVDocsIdentifier(techCVDocsItem));
      const techCVDocsToAdd = techCVDocs.filter(techCVDocsItem => {
        const techCVDocsIdentifier = this.getTechCVDocsIdentifier(techCVDocsItem);
        if (techCVDocsCollectionIdentifiers.includes(techCVDocsIdentifier)) {
          return false;
        }
        techCVDocsCollectionIdentifiers.push(techCVDocsIdentifier);
        return true;
      });
      return [...techCVDocsToAdd, ...techCVDocsCollection];
    }
    return techCVDocsCollection;
  }
}
