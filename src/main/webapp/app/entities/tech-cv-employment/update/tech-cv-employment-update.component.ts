import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';
import { TechnicalCVService } from 'app/entities/technical-cv/service/technical-cv.service';
import { ITechCVEmployment } from '../tech-cv-employment.model';
import { TechCVEmploymentService } from '../service/tech-cv-employment.service';
import { TechCVEmploymentFormService, TechCVEmploymentFormGroup } from './tech-cv-employment-form.service';

@Component({
  standalone: true,
  selector: 'jhi-tech-cv-employment-update',
  templateUrl: './tech-cv-employment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TechCVEmploymentUpdateComponent implements OnInit {
  isSaving = false;
  techCVEmployment: ITechCVEmployment | null = null;

  technicalCVSSharedCollection: ITechnicalCV[] = [];

  protected techCVEmploymentService = inject(TechCVEmploymentService);
  protected techCVEmploymentFormService = inject(TechCVEmploymentFormService);
  protected technicalCVService = inject(TechnicalCVService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TechCVEmploymentFormGroup = this.techCVEmploymentFormService.createTechCVEmploymentFormGroup();

  compareTechnicalCV = (o1: ITechnicalCV | null, o2: ITechnicalCV | null): boolean => this.technicalCVService.compareTechnicalCV(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ techCVEmployment }) => {
      this.techCVEmployment = techCVEmployment;
      if (techCVEmployment) {
        this.updateForm(techCVEmployment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const techCVEmployment = this.techCVEmploymentFormService.getTechCVEmployment(this.editForm);
    if (techCVEmployment.id !== null) {
      this.subscribeToSaveResponse(this.techCVEmploymentService.update(techCVEmployment));
    } else {
      this.subscribeToSaveResponse(this.techCVEmploymentService.create(techCVEmployment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITechCVEmployment>>): void {
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

  protected updateForm(techCVEmployment: ITechCVEmployment): void {
    this.techCVEmployment = techCVEmployment;
    this.techCVEmploymentFormService.resetForm(this.editForm, techCVEmployment);

    this.technicalCVSSharedCollection = this.technicalCVService.addTechnicalCVToCollectionIfMissing<ITechnicalCV>(
      this.technicalCVSSharedCollection,
      techCVEmployment.technicalCV,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.technicalCVService
      .query()
      .pipe(map((res: HttpResponse<ITechnicalCV[]>) => res.body ?? []))
      .pipe(
        map((technicalCVS: ITechnicalCV[]) =>
          this.technicalCVService.addTechnicalCVToCollectionIfMissing<ITechnicalCV>(technicalCVS, this.techCVEmployment?.technicalCV),
        ),
      )
      .subscribe((technicalCVS: ITechnicalCV[]) => (this.technicalCVSSharedCollection = technicalCVS));
  }
}
