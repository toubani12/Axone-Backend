import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITechCVAchievement, NewTechCVAchievement } from '../tech-cv-achievement.model';

export type PartialUpdateTechCVAchievement = Partial<ITechCVAchievement> & Pick<ITechCVAchievement, 'id'>;

export type EntityResponseType = HttpResponse<ITechCVAchievement>;
export type EntityArrayResponseType = HttpResponse<ITechCVAchievement[]>;

@Injectable({ providedIn: 'root' })
export class TechCVAchievementService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tech-cv-achievements');

  create(techCVAchievement: NewTechCVAchievement): Observable<EntityResponseType> {
    return this.http.post<ITechCVAchievement>(this.resourceUrl, techCVAchievement, { observe: 'response' });
  }

  update(techCVAchievement: ITechCVAchievement): Observable<EntityResponseType> {
    return this.http.put<ITechCVAchievement>(
      `${this.resourceUrl}/${this.getTechCVAchievementIdentifier(techCVAchievement)}`,
      techCVAchievement,
      { observe: 'response' },
    );
  }

  partialUpdate(techCVAchievement: PartialUpdateTechCVAchievement): Observable<EntityResponseType> {
    return this.http.patch<ITechCVAchievement>(
      `${this.resourceUrl}/${this.getTechCVAchievementIdentifier(techCVAchievement)}`,
      techCVAchievement,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITechCVAchievement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITechCVAchievement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTechCVAchievementIdentifier(techCVAchievement: Pick<ITechCVAchievement, 'id'>): number {
    return techCVAchievement.id;
  }

  compareTechCVAchievement(o1: Pick<ITechCVAchievement, 'id'> | null, o2: Pick<ITechCVAchievement, 'id'> | null): boolean {
    return o1 && o2 ? this.getTechCVAchievementIdentifier(o1) === this.getTechCVAchievementIdentifier(o2) : o1 === o2;
  }

  addTechCVAchievementToCollectionIfMissing<Type extends Pick<ITechCVAchievement, 'id'>>(
    techCVAchievementCollection: Type[],
    ...techCVAchievementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const techCVAchievements: Type[] = techCVAchievementsToCheck.filter(isPresent);
    if (techCVAchievements.length > 0) {
      const techCVAchievementCollectionIdentifiers = techCVAchievementCollection.map(techCVAchievementItem =>
        this.getTechCVAchievementIdentifier(techCVAchievementItem),
      );
      const techCVAchievementsToAdd = techCVAchievements.filter(techCVAchievementItem => {
        const techCVAchievementIdentifier = this.getTechCVAchievementIdentifier(techCVAchievementItem);
        if (techCVAchievementCollectionIdentifiers.includes(techCVAchievementIdentifier)) {
          return false;
        }
        techCVAchievementCollectionIdentifiers.push(techCVAchievementIdentifier);
        return true;
      });
      return [...techCVAchievementsToAdd, ...techCVAchievementCollection];
    }
    return techCVAchievementCollection;
  }
}
