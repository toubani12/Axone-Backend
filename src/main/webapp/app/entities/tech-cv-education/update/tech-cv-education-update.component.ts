import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';
import { TechnicalCVService } from 'app/entities/technical-cv/service/technical-cv.service';
import { ITechCVEducation } from '../tech-cv-education.model';
import { TechCVEducationService } from '../service/tech-cv-education.service';
import { TechCVEducationFormService, TechCVEducationFormGroup } from './tech-cv-education-form.service';

@Component({
  standalone: true,
  selector: 'jhi-tech-cv-education-update',
  templateUrl: './tech-cv-education-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TechCVEducationUpdateComponent implements OnInit {
  isSaving = false;
  techCVEducation: ITechCVEducation | null = null;

  technicalCVSSharedCollection: ITechnicalCV[] = [];

  protected techCVEducationService = inject(TechCVEducationService);
  protected techCVEducationFormService = inject(TechCVEducationFormService);
  protected technicalCVService = inject(TechnicalCVService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TechCVEducationFormGroup = this.techCVEducationFormService.createTechCVEducationFormGroup();

  compareTechnicalCV = (o1: ITechnicalCV | null, o2: ITechnicalCV | null): boolean => this.technicalCVService.compareTechnicalCV(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ techCVEducation }) => {
      this.techCVEducation = techCVEducation;
      if (techCVEducation) {
        this.updateForm(techCVEducation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const techCVEducation = this.techCVEducationFormService.getTechCVEducation(this.editForm);
    if (techCVEducation.id !== null) {
      this.subscribeToSaveResponse(this.techCVEducationService.update(techCVEducation));
    } else {
      this.subscribeToSaveResponse(this.techCVEducationService.create(techCVEducation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITechCVEducation>>): void {
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

  protected updateForm(techCVEducation: ITechCVEducation): void {
    this.techCVEducation = techCVEducation;
    this.techCVEducationFormService.resetForm(this.editForm, techCVEducation);

    this.technicalCVSSharedCollection = this.technicalCVService.addTechnicalCVToCollectionIfMissing<ITechnicalCV>(
      this.technicalCVSSharedCollection,
      techCVEducation.technicalCV,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.technicalCVService
      .query()
      .pipe(map((res: HttpResponse<ITechnicalCV[]>) => res.body ?? []))
      .pipe(
        map((technicalCVS: ITechnicalCV[]) =>
          this.technicalCVService.addTechnicalCVToCollectionIfMissing<ITechnicalCV>(technicalCVS, this.techCVEducation?.technicalCV),
        ),
      )
      .subscribe((technicalCVS: ITechnicalCV[]) => (this.technicalCVSSharedCollection = technicalCVS));
  }
}
