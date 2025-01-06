import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITechCVAltActivities, NewTechCVAltActivities } from '../tech-cv-alt-activities.model';

export type PartialUpdateTechCVAltActivities = Partial<ITechCVAltActivities> & Pick<ITechCVAltActivities, 'id'>;

export type EntityResponseType = HttpResponse<ITechCVAltActivities>;
export type EntityArrayResponseType = HttpResponse<ITechCVAltActivities[]>;

@Injectable({ providedIn: 'root' })
export class TechCVAltActivitiesService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tech-cv-alt-activities');

  create(techCVAltActivities: NewTechCVAltActivities): Observable<EntityResponseType> {
    return this.http.post<ITechCVAltActivities>(this.resourceUrl, techCVAltActivities, { observe: 'response' });
  }

  update(techCVAltActivities: ITechCVAltActivities): Observable<EntityResponseType> {
    return this.http.put<ITechCVAltActivities>(
      `${this.resourceUrl}/${this.getTechCVAltActivitiesIdentifier(techCVAltActivities)}`,
      techCVAltActivities,
      { observe: 'response' },
    );
  }

  partialUpdate(techCVAltActivities: PartialUpdateTechCVAltActivities): Observable<EntityResponseType> {
    return this.http.patch<ITechCVAltActivities>(
      `${this.resourceUrl}/${this.getTechCVAltActivitiesIdentifier(techCVAltActivities)}`,
      techCVAltActivities,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITechCVAltActivities>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITechCVAltActivities[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTechCVAltActivitiesIdentifier(techCVAltActivities: Pick<ITechCVAltActivities, 'id'>): number {
    return techCVAltActivities.id;
  }

  compareTechCVAltActivities(o1: Pick<ITechCVAltActivities, 'id'> | null, o2: Pick<ITechCVAltActivities, 'id'> | null): boolean {
    return o1 && o2 ? this.getTechCVAltActivitiesIdentifier(o1) === this.getTechCVAltActivitiesIdentifier(o2) : o1 === o2;
  }

  addTechCVAltActivitiesToCollectionIfMissing<Type extends Pick<ITechCVAltActivities, 'id'>>(
    techCVAltActivitiesCollection: Type[],
    ...techCVAltActivitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const techCVAltActivities: Type[] = techCVAltActivitiesToCheck.filter(isPresent);
    if (techCVAltActivities.length > 0) {
      const techCVAltActivitiesCollectionIdentifiers = techCVAltActivitiesCollection.map(techCVAltActivitiesItem =>
        this.getTechCVAltActivitiesIdentifier(techCVAltActivitiesItem),
      );
      const techCVAltActivitiesToAdd = techCVAltActivities.filter(techCVAltActivitiesItem => {
        const techCVAltActivitiesIdentifier = this.getTechCVAltActivitiesIdentifier(techCVAltActivitiesItem);
        if (techCVAltActivitiesCollectionIdentifiers.includes(techCVAltActivitiesIdentifier)) {
          return false;
        }
        techCVAltActivitiesCollectionIdentifiers.push(techCVAltActivitiesIdentifier);
        return true;
      });
      return [...techCVAltActivitiesToAdd, ...techCVAltActivitiesCollection];
    }
    return techCVAltActivitiesCollection;
  }
}
