import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITechCVEducation, NewTechCVEducation } from '../tech-cv-education.model';

export type PartialUpdateTechCVEducation = Partial<ITechCVEducation> & Pick<ITechCVEducation, 'id'>;

export type EntityResponseType = HttpResponse<ITechCVEducation>;
export type EntityArrayResponseType = HttpResponse<ITechCVEducation[]>;

@Injectable({ providedIn: 'root' })
export class TechCVEducationService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tech-cv-educations');

  create(techCVEducation: NewTechCVEducation): Observable<EntityResponseType> {
    return this.http.post<ITechCVEducation>(this.resourceUrl, techCVEducation, { observe: 'response' });
  }

  update(techCVEducation: ITechCVEducation): Observable<EntityResponseType> {
    return this.http.put<ITechCVEducation>(`${this.resourceUrl}/${this.getTechCVEducationIdentifier(techCVEducation)}`, techCVEducation, {
      observe: 'response',
    });
  }

  partialUpdate(techCVEducation: PartialUpdateTechCVEducation): Observable<EntityResponseType> {
    return this.http.patch<ITechCVEducation>(`${this.resourceUrl}/${this.getTechCVEducationIdentifier(techCVEducation)}`, techCVEducation, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITechCVEducation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITechCVEducation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTechCVEducationIdentifier(techCVEducation: Pick<ITechCVEducation, 'id'>): number {
    return techCVEducation.id;
  }

  compareTechCVEducation(o1: Pick<ITechCVEducation, 'id'> | null, o2: Pick<ITechCVEducation, 'id'> | null): boolean {
    return o1 && o2 ? this.getTechCVEducationIdentifier(o1) === this.getTechCVEducationIdentifier(o2) : o1 === o2;
  }

  addTechCVEducationToCollectionIfMissing<Type extends Pick<ITechCVEducation, 'id'>>(
    techCVEducationCollection: Type[],
    ...techCVEducationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const techCVEducations: Type[] = techCVEducationsToCheck.filter(isPresent);
    if (techCVEducations.length > 0) {
      const techCVEducationCollectionIdentifiers = techCVEducationCollection.map(techCVEducationItem =>
        this.getTechCVEducationIdentifier(techCVEducationItem),
      );
      const techCVEducationsToAdd = techCVEducations.filter(techCVEducationItem => {
        const techCVEducationIdentifier = this.getTechCVEducationIdentifier(techCVEducationItem);
        if (techCVEducationCollectionIdentifiers.includes(techCVEducationIdentifier)) {
          return false;
        }
        techCVEducationCollectionIdentifiers.push(techCVEducationIdentifier);
        return true;
      });
      return [...techCVEducationsToAdd, ...techCVEducationCollection];
    }
    return techCVEducationCollection;
  }
}
