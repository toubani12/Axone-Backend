import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAppTest } from 'app/entities/app-test/app-test.model';
import { AppTestService } from 'app/entities/app-test/service/app-test.service';
import { ICustomQuestion } from '../custom-question.model';
import { CustomQuestionService } from '../service/custom-question.service';
import { CustomQuestionFormService, CustomQuestionFormGroup } from './custom-question-form.service';

@Component({
  standalone: true,
  selector: 'jhi-custom-question-update',
  templateUrl: './custom-question-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CustomQuestionUpdateComponent implements OnInit {
  isSaving = false;
  customQuestion: ICustomQuestion | null = null;

  appTestsSharedCollection: IAppTest[] = [];

  protected customQuestionService = inject(CustomQuestionService);
  protected customQuestionFormService = inject(CustomQuestionFormService);
  protected appTestService = inject(AppTestService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustomQuestionFormGroup = this.customQuestionFormService.createCustomQuestionFormGroup();

  compareAppTest = (o1: IAppTest | null, o2: IAppTest | null): boolean => this.appTestService.compareAppTest(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customQuestion }) => {
      this.customQuestion = customQuestion;
      if (customQuestion) {
        this.updateForm(customQuestion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customQuestion = this.customQuestionFormService.getCustomQuestion(this.editForm);
    if (customQuestion.id !== null) {
      this.subscribeToSaveResponse(this.customQuestionService.update(customQuestion));
    } else {
      this.subscribeToSaveResponse(this.customQuestionService.create(customQuestion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomQuestion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(customQuestion: ICustomQuestion): void {
    this.customQuestion = customQuestion;
    this.customQuestionFormService.resetForm(this.editForm, customQuestion);

    this.appTestsSharedCollection = this.appTestService.addAppTestToCollectionIfMissing<IAppTest>(
      this.appTestsSharedCollection,
      customQuestion.appTest,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appTestService
      .query()
      .pipe(map((res: HttpResponse<IAppTest[]>) => res.body ?? []))
      .pipe(
        map((appTests: IAppTest[]) =>
          this.appTestService.addAppTestToCollectionIfMissing<IAppTest>(appTests, this.customQuestion?.appTest),
        ),
      )
      .subscribe((appTests: IAppTest[]) => (this.appTestsSharedCollection = appTests));
  }
}
