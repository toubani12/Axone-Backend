import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITechCVEmployment, NewTechCVEmployment } from '../tech-cv-employment.model';

export type PartialUpdateTechCVEmployment = Partial<ITechCVEmployment> & Pick<ITechCVEmployment, 'id'>;

export type EntityResponseType = HttpResponse<ITechCVEmployment>;
export type EntityArrayResponseType = HttpResponse<ITechCVEmployment[]>;

@Injectable({ providedIn: 'root' })
export class TechCVEmploymentService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tech-cv-employments');

  create(techCVEmployment: NewTechCVEmployment): Observable<EntityResponseType> {
    return this.http.post<ITechCVEmployment>(this.resourceUrl, techCVEmployment, { observe: 'response' });
  }

  update(techCVEmployment: ITechCVEmployment): Observable<EntityResponseType> {
    return this.http.put<ITechCVEmployment>(
      `${this.resourceUrl}/${this.getTechCVEmploymentIdentifier(techCVEmployment)}`,
      techCVEmployment,
      { observe: 'response' },
    );
  }

  partialUpdate(techCVEmployment: PartialUpdateTechCVEmployment): Observable<EntityResponseType> {
    return this.http.patch<ITechCVEmployment>(
      `${this.resourceUrl}/${this.getTechCVEmploymentIdentifier(techCVEmployment)}`,
      techCVEmployment,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITechCVEmployment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITechCVEmployment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTechCVEmploymentIdentifier(techCVEmployment: Pick<ITechCVEmployment, 'id'>): number {
    return techCVEmployment.id;
  }

  compareTechCVEmployment(o1: Pick<ITechCVEmployment, 'id'> | null, o2: Pick<ITechCVEmployment, 'id'> | null): boolean {
    return o1 && o2 ? this.getTechCVEmploymentIdentifier(o1) === this.getTechCVEmploymentIdentifier(o2) : o1 === o2;
  }

  addTechCVEmploymentToCollectionIfMissing<Type extends Pick<ITechCVEmployment, 'id'>>(
    techCVEmploymentCollection: Type[],
    ...techCVEmploymentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const techCVEmployments: Type[] = techCVEmploymentsToCheck.filter(isPresent);
    if (techCVEmployments.length > 0) {
      const techCVEmploymentCollectionIdentifiers = techCVEmploymentCollection.map(techCVEmploymentItem =>
        this.getTechCVEmploymentIdentifier(techCVEmploymentItem),
      );
      const techCVEmploymentsToAdd = techCVEmployments.filter(techCVEmploymentItem => {
        const techCVEmploymentIdentifier = this.getTechCVEmploymentIdentifier(techCVEmploymentItem);
        if (techCVEmploymentCollectionIdentifiers.includes(techCVEmploymentIdentifier)) {
          return false;
        }
        techCVEmploymentCollectionIdentifiers.push(techCVEmploymentIdentifier);
        return true;
      });
      return [...techCVEmploymentsToAdd, ...techCVEmploymentCollection];
    }
    return techCVEmploymentCollection;
  }
}
