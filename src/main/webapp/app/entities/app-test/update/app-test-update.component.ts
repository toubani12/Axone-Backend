import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAppTestType } from 'app/entities/app-test-type/app-test-type.model';
import { AppTestTypeService } from 'app/entities/app-test-type/service/app-test-type.service';
import { IAppTest } from '../app-test.model';
import { AppTestService } from '../service/app-test.service';
import { AppTestFormService, AppTestFormGroup } from './app-test-form.service';

@Component({
  standalone: true,
  selector: 'jhi-app-test-update',
  templateUrl: './app-test-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AppTestUpdateComponent implements OnInit {
  isSaving = false;
  appTest: IAppTest | null = null;

  appTestTypesSharedCollection: IAppTestType[] = [];

  protected appTestService = inject(AppTestService);
  protected appTestFormService = inject(AppTestFormService);
  protected appTestTypeService = inject(AppTestTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AppTestFormGroup = this.appTestFormService.createAppTestFormGroup();

  compareAppTestType = (o1: IAppTestType | null, o2: IAppTestType | null): boolean => this.appTestTypeService.compareAppTestType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appTest }) => {
      this.appTest = appTest;
      if (appTest) {
        this.updateForm(appTest);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appTest = this.appTestFormService.getAppTest(this.editForm);
    if (appTest.id !== null) {
      this.subscribeToSaveResponse(this.appTestService.update(appTest));
    } else {
      this.subscribeToSaveResponse(this.appTestService.create(appTest));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppTest>>): void {
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

  protected updateForm(appTest: IAppTest): void {
    this.appTest = appTest;
    this.appTestFormService.resetForm(this.editForm, appTest);

    this.appTestTypesSharedCollection = this.appTestTypeService.addAppTestTypeToCollectionIfMissing<IAppTestType>(
      this.appTestTypesSharedCollection,
      ...(appTest.types ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appTestTypeService
      .query()
      .pipe(map((res: HttpResponse<IAppTestType[]>) => res.body ?? []))
      .pipe(
        map((appTestTypes: IAppTestType[]) =>
          this.appTestTypeService.addAppTestTypeToCollectionIfMissing<IAppTestType>(appTestTypes, ...(this.appTest?.types ?? [])),
        ),
      )
      .subscribe((appTestTypes: IAppTestType[]) => (this.appTestTypesSharedCollection = appTestTypes));
  }
}
