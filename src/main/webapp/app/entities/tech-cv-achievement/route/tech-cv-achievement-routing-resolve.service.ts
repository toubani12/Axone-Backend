import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITechCVAchievement } from '../tech-cv-achievement.model';
import { TechCVAchievementService } from '../service/tech-cv-achievement.service';

const techCVAchievementResolve = (route: ActivatedRouteSnapshot): Observable<null | ITechCVAchievement> => {
  const id = route.params['id'];
  if (id) {
    return inject(TechCVAchievementService)
      .find(id)
      .pipe(
        mergeMap((techCVAchievement: HttpResponse<ITechCVAchievement>) => {
          if (techCVAchievement.body) {
            return of(techCVAchievement.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default techCVAchievementResolve;
