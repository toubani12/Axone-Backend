import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITechCVHardSkills, NewTechCVHardSkills } from '../tech-cv-hard-skills.model';

export type PartialUpdateTechCVHardSkills = Partial<ITechCVHardSkills> & Pick<ITechCVHardSkills, 'id'>;

export type EntityResponseType = HttpResponse<ITechCVHardSkills>;
export type EntityArrayResponseType = HttpResponse<ITechCVHardSkills[]>;

@Injectable({ providedIn: 'root' })
export class TechCVHardSkillsService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tech-cv-hard-skills');

  create(techCVHardSkills: NewTechCVHardSkills): Observable<EntityResponseType> {
    return this.http.post<ITechCVHardSkills>(this.resourceUrl, techCVHardSkills, { observe: 'response' });
  }

  update(techCVHardSkills: ITechCVHardSkills): Observable<EntityResponseType> {
    return this.http.put<ITechCVHardSkills>(
      `${this.resourceUrl}/${this.getTechCVHardSkillsIdentifier(techCVHardSkills)}`,
      techCVHardSkills,
      { observe: 'response' },
    );
  }

  partialUpdate(techCVHardSkills: PartialUpdateTechCVHardSkills): Observable<EntityResponseType> {
    return this.http.patch<ITechCVHardSkills>(
      `${this.resourceUrl}/${this.getTechCVHardSkillsIdentifier(techCVHardSkills)}`,
      techCVHardSkills,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITechCVHardSkills>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITechCVHardSkills[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTechCVHardSkillsIdentifier(techCVHardSkills: Pick<ITechCVHardSkills, 'id'>): number {
    return techCVHardSkills.id;
  }

  compareTechCVHardSkills(o1: Pick<ITechCVHardSkills, 'id'> | null, o2: Pick<ITechCVHardSkills, 'id'> | null): boolean {
    return o1 && o2 ? this.getTechCVHardSkillsIdentifier(o1) === this.getTechCVHardSkillsIdentifier(o2) : o1 === o2;
  }

  addTechCVHardSkillsToCollectionIfMissing<Type extends Pick<ITechCVHardSkills, 'id'>>(
    techCVHardSkillsCollection: Type[],
    ...techCVHardSkillsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const techCVHardSkills: Type[] = techCVHardSkillsToCheck.filter(isPresent);
    if (techCVHardSkills.length > 0) {
      const techCVHardSkillsCollectionIdentifiers = techCVHardSkillsCollection.map(techCVHardSkillsItem =>
        this.getTechCVHardSkillsIdentifier(techCVHardSkillsItem),
      );
      const techCVHardSkillsToAdd = techCVHardSkills.filter(techCVHardSkillsItem => {
        const techCVHardSkillsIdentifier = this.getTechCVHardSkillsIdentifier(techCVHardSkillsItem);
        if (techCVHardSkillsCollectionIdentifiers.includes(techCVHardSkillsIdentifier)) {
          return false;
        }
        techCVHardSkillsCollectionIdentifiers.push(techCVHardSkillsIdentifier);
        return true;
      });
      return [...techCVHardSkillsToAdd, ...techCVHardSkillsCollection];
    }
    return techCVHardSkillsCollection;
  }
}
