import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAppTest } from 'app/entities/app-test/app-test.model';
import { AppTestService } from 'app/entities/app-test/service/app-test.service';
import { IAppTestType } from '../app-test-type.model';
import { AppTestTypeService } from '../service/app-test-type.service';
import { AppTestTypeFormService, AppTestTypeFormGroup } from './app-test-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-app-test-type-update',
  templateUrl: './app-test-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AppTestTypeUpdateComponent implements OnInit {
  isSaving = false;
  appTestType: IAppTestType | null = null;

  appTestsSharedCollection: IAppTest[] = [];

  protected appTestTypeService = inject(AppTestTypeService);
  protected appTestTypeFormService = inject(AppTestTypeFormService);
  protected appTestService = inject(AppTestService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AppTestTypeFormGroup = this.appTestTypeFormService.createAppTestTypeFormGroup();

  compareAppTest = (o1: IAppTest | null, o2: IAppTest | null): boolean => this.appTestService.compareAppTest(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appTestType }) => {
      this.appTestType = appTestType;
      if (appTestType) {
        this.updateForm(appTestType);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appTestType = this.appTestTypeFormService.getAppTestType(this.editForm);
    if (appTestType.id !== null) {
      this.subscribeToSaveResponse(this.appTestTypeService.update(appTestType));
    } else {
      this.subscribeToSaveResponse(this.appTestTypeService.create(appTestType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppTestType>>): void {
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

  protected updateForm(appTestType: IAppTestType): void {
    this.appTestType = appTestType;
    this.appTestTypeFormService.resetForm(this.editForm, appTestType);

    this.appTestsSharedCollection = this.appTestService.addAppTestToCollectionIfMissing<IAppTest>(
      this.appTestsSharedCollection,
      ...(appTestType.appTests ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appTestService
      .query()
      .pipe(map((res: HttpResponse<IAppTest[]>) => res.body ?? []))
      .pipe(
        map((appTests: IAppTest[]) =>
          this.appTestService.addAppTestToCollectionIfMissing<IAppTest>(appTests, ...(this.appTestType?.appTests ?? [])),
        ),
      )
      .subscribe((appTests: IAppTest[]) => (this.appTestsSharedCollection = appTests));
  }
}
