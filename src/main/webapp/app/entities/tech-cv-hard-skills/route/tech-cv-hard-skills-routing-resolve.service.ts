import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITechCVHardSkills } from '../tech-cv-hard-skills.model';
import { TechCVHardSkillsService } from '../service/tech-cv-hard-skills.service';

const techCVHardSkillsResolve = (route: ActivatedRouteSnapshot): Observable<null | ITechCVHardSkills> => {
  const id = route.params['id'];
  if (id) {
    return inject(TechCVHardSkillsService)
      .find(id)
      .pipe(
        mergeMap((techCVHardSkills: HttpResponse<ITechCVHardSkills>) => {
          if (techCVHardSkills.body) {
            return of(techCVHardSkills.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default techCVHardSkillsResolve;
