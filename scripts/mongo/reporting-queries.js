// Ejecutar con:
// mongosh "mongodb://localhost:27017/hospital_reporting" scripts/mongo/reporting-queries.js

print("Colecciones:");
printjson(db.getCollectionNames());

print("Migraciones aplicadas:");
db.DATABASECHANGELOG.find().sort({ ORDEREXECUTED: 1 }).forEach(printjson);

print("Índices de consultation_view:");
db.consultation_view.getIndexes().forEach(printjson);

print("Índices de appointment_view:");
db.appointment_view.getIndexes().forEach(printjson);

print("Últimas 20 citas:");
db.appointment_view.find({})
    .sort({ appointmentDateTime: -1 })
    .limit(20)
    .forEach(printjson);

print("Últimas 20 consultas:");
db.consultation_view.find({})
    .sort({ consultationDate: -1 })
    .limit(20)
    .forEach(printjson);

print("Consultas completadas de los últimos 30 días:");
db.consultation_view.find({
    status: "COMPLETED",
    consultationDate: { $gte: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000) }
}).sort({ consultationDate: -1 }).forEach(printjson);

print("Consultas agrupadas por estado:");
db.consultation_view.aggregate([
    { $group: { _id: "$status", total: { $sum: 1 } } },
    { $sort: { total: -1 } }
]).forEach(printjson);

print("Diagnósticos más frecuentes:");
db.consultation_view.aggregate([
    { $unwind: "$diagnoses" },
    { $group: { _id: "$diagnoses.icd10Code", total: { $sum: 1 } } },
    { $sort: { total: -1 } },
    { $limit: 5 }
]).forEach(printjson);
