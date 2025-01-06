import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICustomQuestion } from '../custom-question.model';
import { CustomQuestionService } from '../service/custom-question.service';

const customQuestionResolve = (route: ActivatedRouteSnapshot): Observable<null | ICustomQuestion> => {
  const id = route.params['id'];
  if (id) {
    return inject(CustomQuestionService)
      .find(id)
      .pipe(
        mergeMap((customQuestion: HttpResponse<ICustomQuestion>) => {
          if (customQuestion.body) {
            return of(customQuestion.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default customQuestionResolve;
